package com.smartplacement.service;

import com.smartplacement.dto.DashboardStatsResponse;
import com.smartplacement.entity.Application;
import com.smartplacement.entity.StudentProfile;
import com.smartplacement.enums.ApplicationStatus;
import com.smartplacement.repository.ApplicationRepository;
import com.smartplacement.repository.JobPostingRepository;
import com.smartplacement.repository.StudentProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final StudentProfileRepository studentProfileRepository;
    private final JobPostingRepository jobPostingRepository;
    private final ApplicationRepository applicationRepository;

    @Transactional(readOnly = true)
    public DashboardStatsResponse getOfficerDashboardStats() {
        long totalStudents = studentProfileRepository.count();
        long placedStudents = studentProfileRepository.countPlacedStudents();
        double placementPercentage = totalStudents > 0 ? (double) placedStudents / totalStudents * 100.0 : 0.0;

        long activeDrives = jobPostingRepository.countActiveDrives();
        long totalApplications = applicationRepository.count();
        long totalSelections = applicationRepository.countTotalSelections();

        Double avgPackage = studentProfileRepository.getAveragePlacedPackage();
        Double highestPackage = studentProfileRepository.getHighestPlacedPackage();

        // Branch-wise placements
        List<StudentProfile> allStudents = studentProfileRepository.findAll();
        Map<String, Long> branchWisePlacements = allStudents.stream()
                .filter(s -> Boolean.TRUE.equals(s.getIsPlaced()) && s.getBranch() != null)
                .collect(Collectors.groupingBy(StudentProfile::getBranch, Collectors.counting()));

        // Status-wise applications
        List<Application> allApplications = applicationRepository.findAll();
        Map<String, Long> statusWiseMap = allApplications.stream()
                .collect(Collectors.groupingBy(a -> a.getStatus().name(), Collectors.counting()));

        return DashboardStatsResponse.builder()
                .totalStudents(totalStudents)
                .placedStudents(placedStudents)
                .placementPercentage(Math.round(placementPercentage * 100.0) / 100.0)
                .activeDrives(activeDrives)
                .totalApplications(totalApplications)
                .totalSelections(totalSelections)
                .averagePackageLpa(avgPackage != null ? Math.round(avgPackage * 100.0) / 100.0 : 0.0)
                .highestPackageLpa(highestPackage != null ? highestPackage : 0.0)
                .branchWisePlacement(branchWisePlacements)
                .statusWiseApplications(statusWiseMap)
                .build();
    }

    @Transactional(readOnly = true)
    public DashboardStatsResponse getStudentDashboardStats(Long userId) {
        List<Application> userApps = applicationRepository.findByStudentUserId(userId);

        long appliedCount = userApps.size();
        long shortlistedCount = userApps.stream().filter(a ->
                a.getStatus() == ApplicationStatus.SHORTLISTED ||
                a.getStatus() == ApplicationStatus.ASSESSMENT ||
                a.getStatus() == ApplicationStatus.TECHNICAL_ROUND ||
                a.getStatus() == ApplicationStatus.HR_ROUND ||
                a.getStatus() == ApplicationStatus.SELECTED
        ).count();

        long interviewCount = userApps.stream().filter(a ->
                a.getStatus() == ApplicationStatus.TECHNICAL_ROUND ||
                a.getStatus() == ApplicationStatus.HR_ROUND
        ).count();

        long offersCount = userApps.stream().filter(a -> a.getStatus() == ApplicationStatus.SELECTED).count();

        Map<String, Long> statusMap = userApps.stream()
                .collect(Collectors.groupingBy(a -> a.getStatus().name(), Collectors.counting()));

        return DashboardStatsResponse.builder()
                .studentAppliedCount(appliedCount)
                .studentShortlistedCount(shortlistedCount)
                .studentInterviewCount(interviewCount)
                .studentOffersCount(offersCount)
                .statusWiseApplications(statusMap)
                .build();
    }
}
