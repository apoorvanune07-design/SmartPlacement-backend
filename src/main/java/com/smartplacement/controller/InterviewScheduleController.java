package com.smartplacement.controller;

import com.smartplacement.dto.ApiResponse;
import com.smartplacement.dto.InterviewScheduleRequest;
import com.smartplacement.entity.InterviewSchedule;
import com.smartplacement.security.UserPrincipal;
import com.smartplacement.service.InterviewScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewScheduleController {

    private final InterviewScheduleService interviewScheduleService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_OFFICER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<InterviewSchedule>> scheduleInterview(
            @Valid @RequestBody InterviewScheduleRequest request
    ) {
        InterviewSchedule schedule = interviewScheduleService.scheduleInterview(request);
        return ResponseEntity.ok(ApiResponse.success("Interview scheduled successfully", schedule));
    }

    @GetMapping("/my-interviews")
    public ResponseEntity<ApiResponse<List<InterviewSchedule>>> getMyInterviews(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        List<InterviewSchedule> schedules = interviewScheduleService.getInterviewsForStudent(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(schedules));
    }

    @GetMapping("/upcoming")
    @PreAuthorize("hasRole('ROLE_OFFICER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<InterviewSchedule>>> getUpcomingInterviews() {
        List<InterviewSchedule> schedules = interviewScheduleService.getUpcomingInterviews();
        return ResponseEntity.ok(ApiResponse.success(schedules));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_OFFICER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<InterviewSchedule>> updateInterview(
            @PathVariable Long id,
            @Valid @RequestBody InterviewScheduleRequest request
    ) {
        InterviewSchedule updated = interviewScheduleService.updateInterview(id, request);
        return ResponseEntity.ok(ApiResponse.success("Interview updated", updated));
    }
}
