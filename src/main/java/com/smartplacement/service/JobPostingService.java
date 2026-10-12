package com.smartplacement.service;

import com.smartplacement.dto.EligibilityCheckResponse;
import com.smartplacement.dto.JobPostingRequest;
import com.smartplacement.entity.Company;
import com.smartplacement.entity.JobPosting;
import com.smartplacement.entity.StudentProfile;
import com.smartplacement.enums.DriveStatus;
import com.smartplacement.exception.ResourceNotFoundException;
import com.smartplacement.repository.JobPostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;
    private final CompanyService companyService;
    private final StudentProfileService studentProfileService;
    private final EligibilityService eligibilityService;

    @Transactional(readOnly = true)
    public List<JobPosting> getAllJobPostings(Boolean onlyActive) {
        if (Boolean.TRUE.equals(onlyActive)) {
            return jobPostingRepository.findActiveOpenDrives(LocalDate.now());
        }
        return jobPostingRepository.findAll();
    }

    @Transactional(readOnly = true)
    public JobPosting getJobPostingById(Long id) {
        return jobPostingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobPosting", "id", id));
    }

    @Transactional
    public JobPosting createJobPosting(JobPostingRequest request) {
        Company company = companyService.getCompanyById(request.getCompanyId());

        JobPosting jobPosting = JobPosting.builder()
                .title(request.getTitle())
                .company(company)
                .jobType(request.getJobType())
                .role(request.getRole())
                .location(request.getLocation())
                .ctcLpa(request.getCtcLpa())
                .stipendPerMonth(request.getStipendPerMonth())
                .description(request.getDescription())
                .requirements(request.getRequirements())
                .minCgpa(request.getMinCgpa() != null ? request.getMinCgpa() : 6.0)
                .minTenthPercentage(request.getMinTenthPercentage() != null ? request.getMinTenthPercentage() : 60.0)
                .minTwelfthPercentage(request.getMinTwelfthPercentage() != null ? request.getMinTwelfthPercentage() : 60.0)
                .maxBacklogsAllowed(request.getMaxBacklogsAllowed() != null ? request.getMaxBacklogsAllowed() : 0)
                .allowedBranches(request.getAllowedBranches() != null ? request.getAllowedBranches() : "ALL")
                .targetPassingYear(request.getTargetPassingYear())
                .rounds(request.getRounds() != null ? request.getRounds() : "Online Test -> Tech Round -> HR Round")
                .applicationDeadline(request.getApplicationDeadline())
                .driveDate(request.getDriveDate())
                .driveStatus(request.getDriveStatus() != null ? request.getDriveStatus() : DriveStatus.ACTIVE)
                .build();

        return jobPostingRepository.save(jobPosting);
    }

    @Transactional
    public JobPosting updateJobPosting(Long id, JobPostingRequest request) {
        JobPosting jobPosting = getJobPostingById(id);

        if (request.getCompanyId() != null) {
            Company company = companyService.getCompanyById(request.getCompanyId());
            jobPosting.setCompany(company);
        }

        jobPosting.setTitle(request.getTitle());
        if (request.getJobType() != null) jobPosting.setJobType(request.getJobType());
        jobPosting.setRole(request.getRole());
        jobPosting.setLocation(request.getLocation());
        jobPosting.setCtcLpa(request.getCtcLpa());
        jobPosting.setStipendPerMonth(request.getStipendPerMonth());
        jobPosting.setDescription(request.getDescription());
        jobPosting.setRequirements(request.getRequirements());
        if (request.getMinCgpa() != null) jobPosting.setMinCgpa(request.getMinCgpa());
        if (request.getMinTenthPercentage() != null) jobPosting.setMinTenthPercentage(request.getMinTenthPercentage());
        if (request.getMinTwelfthPercentage() != null) jobPosting.setMinTwelfthPercentage(request.getMinTwelfthPercentage());
        if (request.getMaxBacklogsAllowed() != null) jobPosting.setMaxBacklogsAllowed(request.getMaxBacklogsAllowed());
        if (request.getAllowedBranches() != null) jobPosting.setAllowedBranches(request.getAllowedBranches());
        if (request.getTargetPassingYear() != null) jobPosting.setTargetPassingYear(request.getTargetPassingYear());
        if (request.getRounds() != null) jobPosting.setRounds(request.getRounds());
        if (request.getApplicationDeadline() != null) jobPosting.setApplicationDeadline(request.getApplicationDeadline());
        if (request.getDriveDate() != null) jobPosting.setDriveDate(request.getDriveDate());
        if (request.getDriveStatus() != null) jobPosting.setDriveStatus(request.getDriveStatus());

        return jobPostingRepository.save(jobPosting);
    }

    @Transactional
    public void deleteJobPosting(Long id) {
        JobPosting jobPosting = getJobPostingById(id);
        jobPostingRepository.delete(jobPosting);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getJobsWithEligibilityForStudent(Long userId) {
        StudentProfile student = studentProfileService.getProfileByUserId(userId);
        List<JobPosting> jobs = jobPostingRepository.findActiveOpenDrives(LocalDate.now());

        return jobs.stream().map(job -> {
            EligibilityCheckResponse eligibility = eligibilityService.checkEligibility(student, job);
            Map<String, Object> map = new HashMap<>();
            map.put("job", job);
            map.put("eligibility", eligibility);
            return map;
        }).collect(Collectors.toList());
    }
}
