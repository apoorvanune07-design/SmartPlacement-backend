package com.smartplacement.service;

import com.smartplacement.dto.ApplicationRequest;
import com.smartplacement.dto.ApplicationStatusUpdateRequest;
import com.smartplacement.dto.EligibilityCheckResponse;
import com.smartplacement.entity.Application;
import com.smartplacement.entity.JobPosting;
import com.smartplacement.entity.StudentProfile;
import com.smartplacement.enums.ApplicationStatus;
import com.smartplacement.enums.DriveStatus;
import com.smartplacement.exception.BadRequestException;
import com.smartplacement.exception.ResourceNotFoundException;
import com.smartplacement.repository.ApplicationRepository;
import com.smartplacement.repository.JobPostingRepository;
import com.smartplacement.repository.StudentProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobPostingRepository jobPostingRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final StudentProfileService studentProfileService;
    private final EligibilityService eligibilityService;

    @Transactional
    public Application applyForJob(Long userId, ApplicationRequest request) {
        StudentProfile student = studentProfileService.getProfileByUserId(userId);
        JobPosting jobPosting = jobPostingRepository.findById(request.getJobPostingId())
                .orElseThrow(() -> new ResourceNotFoundException("JobPosting", "id", request.getJobPostingId()));

        // Check if drive is still active
        if (jobPosting.getDriveStatus() != DriveStatus.ACTIVE) {
            throw new BadRequestException("This recruitment drive is not active for new applications.");
        }

        // Check deadline
        if (jobPosting.getApplicationDeadline() != null && jobPosting.getApplicationDeadline().isBefore(LocalDate.now())) {
            throw new BadRequestException("The application deadline for this drive has passed.");
        }

        // Check for duplicate application
        if (applicationRepository.existsByStudentIdAndJobPostingId(student.getId(), jobPosting.getId())) {
            throw new BadRequestException("You have already submitted an application for this drive.");
        }

        // Check student eligibility
        EligibilityCheckResponse eligibility = eligibilityService.checkEligibility(student, jobPosting);
        if (!eligibility.isEligible()) {
            throw new BadRequestException("You are not eligible for this opportunity: " + String.join("; ", eligibility.getFailedCriteria()));
        }

        Application application = Application.builder()
                .student(student)
                .jobPosting(jobPosting)
                .status(ApplicationStatus.APPLIED)
                .currentRound("Applied / Initial Review")
                .coverNote(request.getCoverNote())
                .build();

        return applicationRepository.save(application);
    }

    @Transactional(readOnly = true)
    public List<Application> getApplicationsByStudent(Long userId) {
        return applicationRepository.findByStudentUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Application> getApplicationsByJobPosting(Long jobPostingId) {
        return applicationRepository.findByJobPostingId(jobPostingId);
    }

    @Transactional(readOnly = true)
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", id));
    }

    @Transactional
    public Application updateApplicationStatus(Long applicationId, ApplicationStatusUpdateRequest request) {
        Application application = getApplicationById(applicationId);

        application.setStatus(request.getStatus());

        if (request.getOfficerNotes() != null) {
            application.setOfficerNotes(request.getOfficerNotes());
        }

        if (request.getCurrentRound() != null) {
            application.setCurrentRound(request.getCurrentRound());
        }

        // If selected / offer extended
        if (request.getStatus() == ApplicationStatus.SELECTED) {
            if (request.getOfferedCtc() != null) {
                application.setOfferedCtc(request.getOfferedCtc());
            } else if (application.getJobPosting().getCtcLpa() != null) {
                application.setOfferedCtc(application.getJobPosting().getCtcLpa());
            }

            if (request.getOfferLetterUrl() != null) {
                application.setOfferLetterUrl(request.getOfferLetterUrl());
            }

            // Update student placement flag
            StudentProfile student = application.getStudent();
            student.setIsPlaced(true);
            student.setPlacedCompany(application.getJobPosting().getCompany().getName());
            student.setPackageLpa(application.getOfferedCtc());
            studentProfileRepository.save(student);
        }

        if (request.getOfferAccepted() != null) {
            application.setOfferAccepted(request.getOfferAccepted());
        }

        return applicationRepository.save(application);
    }

    @Transactional
    public Application respondToOffer(Long applicationId, Long userId, boolean accept) {
        Application application = getApplicationById(applicationId);

        if (!application.getStudent().getUser().getId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to this application.");
        }

        if (application.getStatus() != ApplicationStatus.SELECTED) {
            throw new BadRequestException("Offer has not been extended for this application.");
        }

        application.setOfferAccepted(accept);
        if (accept) {
            StudentProfile student = application.getStudent();
            student.setIsPlaced(true);
            student.setPlacedCompany(application.getJobPosting().getCompany().getName());
            student.setPackageLpa(application.getOfferedCtc());
            studentProfileRepository.save(student);
        }

        return applicationRepository.save(application);
    }
}
