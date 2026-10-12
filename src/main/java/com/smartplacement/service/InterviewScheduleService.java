package com.smartplacement.service;

import com.smartplacement.dto.InterviewScheduleRequest;
import com.smartplacement.entity.Application;
import com.smartplacement.entity.InterviewSchedule;
import com.smartplacement.enums.ApplicationStatus;
import com.smartplacement.exception.ResourceNotFoundException;
import com.smartplacement.repository.ApplicationRepository;
import com.smartplacement.repository.InterviewScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewScheduleService {

    private final InterviewScheduleRepository interviewScheduleRepository;
    private final ApplicationRepository applicationRepository;

    @Transactional
    public InterviewSchedule scheduleInterview(InterviewScheduleRequest request) {
        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", request.getApplicationId()));

        InterviewSchedule interview = InterviewSchedule.builder()
                .application(application)
                .roundName(request.getRoundName())
                .interviewDate(request.getInterviewDate())
                .interviewTime(request.getInterviewTime())
                .mode(request.getMode())
                .meetingLinkOrVenue(request.getMeetingLinkOrVenue())
                .interviewerName(request.getInterviewerName())
                .interviewerEmail(request.getInterviewerEmail())
                .instructions(request.getInstructions())
                .status("SCHEDULED")
                .build();

        // Update application's current round and status
        application.setCurrentRound(request.getRoundName());
        if (application.getStatus() == ApplicationStatus.APPLIED || application.getStatus() == ApplicationStatus.UNDER_REVIEW) {
            application.setStatus(ApplicationStatus.SHORTLISTED);
        }
        applicationRepository.save(application);

        return interviewScheduleRepository.save(interview);
    }

    @Transactional(readOnly = true)
    public List<InterviewSchedule> getInterviewsForStudent(Long userId) {
        return interviewScheduleRepository.findByStudentUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<InterviewSchedule> getUpcomingInterviews() {
        return interviewScheduleRepository.findUpcomingInterviews(LocalDate.now());
    }

    @Transactional
    public InterviewSchedule updateInterview(Long id, InterviewScheduleRequest request) {
        InterviewSchedule interview = interviewScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InterviewSchedule", "id", id));

        if (request.getRoundName() != null) interview.setRoundName(request.getRoundName());
        if (request.getInterviewDate() != null) interview.setInterviewDate(request.getInterviewDate());
        if (request.getInterviewTime() != null) interview.setInterviewTime(request.getInterviewTime());
        if (request.getMode() != null) interview.setMode(request.getMode());
        if (request.getMeetingLinkOrVenue() != null) interview.setMeetingLinkOrVenue(request.getMeetingLinkOrVenue());
        if (request.getInterviewerName() != null) interview.setInterviewerName(request.getInterviewerName());
        if (request.getInterviewerEmail() != null) interview.setInterviewerEmail(request.getInterviewerEmail());
        if (request.getInstructions() != null) interview.setInstructions(request.getInstructions());
        if (request.getStatus() != null) interview.setStatus(request.getStatus());
        if (request.getFeedback() != null) interview.setFeedback(request.getFeedback());

        return interviewScheduleRepository.save(interview);
    }
}
