package com.smartplacement.dto;

import com.smartplacement.enums.InterviewMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewScheduleRequest {

    @NotNull(message = "Application ID is required")
    private Long applicationId;

    @NotBlank(message = "Round name is required")
    private String roundName;

    @NotNull(message = "Interview date is required")
    private LocalDate interviewDate;

    @NotNull(message = "Interview time is required")
    private LocalTime interviewTime;

    private InterviewMode mode = InterviewMode.ONLINE;
    private String meetingLinkOrVenue;
    private String interviewerName;
    private String interviewerEmail;
    private String instructions;
    private String status = "SCHEDULED";
    private String feedback;
}
