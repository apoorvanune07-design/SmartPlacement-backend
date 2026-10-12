package com.smartplacement.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfileRequest {

    private String rollNumber;
    private String branch;
    private String degree;
    private Integer passingYear;

    @DecimalMin(value = "0.0", message = "CGPA cannot be negative")
    @DecimalMax(value = "10.0", message = "CGPA cannot exceed 10.0")
    private Double cgpa;

    private Double tenthPercentage;
    private Double twelfthPercentage;

    private Integer activeBacklogs;
    private Integer totalBacklogs;

    private String skills;
    private String resumeUrl;
    private String githubUrl;
    private String linkedinUrl;
    private String portfolioUrl;
    private String bio;
}
