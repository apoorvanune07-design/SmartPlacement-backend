package com.smartplacement.dto;

import com.smartplacement.enums.DriveStatus;
import com.smartplacement.enums.JobType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobPostingRequest {

    @NotBlank(message = "Job title is required")
    private String title;

    @NotNull(message = "Company ID is required")
    private Long companyId;

    private JobType jobType = JobType.FULL_TIME;
    private String role;
    private String location;
    private Double ctcLpa;
    private Double stipendPerMonth;
    private String description;
    private String requirements;

    // Eligibility Criteria
    private Double minCgpa = 6.0;
    private Double minTenthPercentage = 60.0;
    private Double minTwelfthPercentage = 60.0;
    private Integer maxBacklogsAllowed = 0;
    private String allowedBranches = "CSE,IT,ECE";
    private Integer targetPassingYear;

    private String rounds;
    private LocalDate applicationDeadline;
    private LocalDate driveDate;
    private DriveStatus driveStatus = DriveStatus.ACTIVE;
}
