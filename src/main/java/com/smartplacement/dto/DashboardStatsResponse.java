package com.smartplacement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsResponse {
    // Placement officer statistics
    private long totalStudents;
    private long placedStudents;
    private double placementPercentage;
    private long activeDrives;
    private long totalApplications;
    private long totalSelections;
    private Double averagePackageLpa;
    private Double highestPackageLpa;
    private Map<String, Long> branchWisePlacement;
    private Map<String, Long> statusWiseApplications;

    // Student specific stats (if called by student)
    private long studentAppliedCount;
    private long studentShortlistedCount;
    private long studentInterviewCount;
    private long studentOffersCount;
}
