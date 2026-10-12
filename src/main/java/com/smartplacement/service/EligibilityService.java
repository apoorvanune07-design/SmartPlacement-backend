package com.smartplacement.service;

import com.smartplacement.dto.EligibilityCheckResponse;
import com.smartplacement.entity.JobPosting;
import com.smartplacement.entity.StudentProfile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class EligibilityService {

    public EligibilityCheckResponse checkEligibility(StudentProfile student, JobPosting job) {
        List<String> passed = new ArrayList<>();
        List<String> failed = new ArrayList<>();

        if (student == null) {
            return EligibilityCheckResponse.builder()
                    .eligible(false)
                    .failedCriteria(List.of("Student profile not found. Please complete your profile first."))
                    .summary("Incomplete profile")
                    .build();
        }

        // 1. CGPA Criterion
        if (job.getMinCgpa() != null) {
            double studentCgpa = student.getCgpa() != null ? student.getCgpa() : 0.0;
            if (studentCgpa >= job.getMinCgpa()) {
                passed.add(String.format("CGPA requirement met: %.2f (Min required: %.2f)", studentCgpa, job.getMinCgpa()));
            } else {
                failed.add(String.format("CGPA too low: %.2f (Min required: %.2f)", studentCgpa, job.getMinCgpa()));
            }
        }

        // 2. 10th Percentage Criterion
        if (job.getMinTenthPercentage() != null) {
            double studentTenth = student.getTenthPercentage() != null ? student.getTenthPercentage() : 0.0;
            if (studentTenth >= job.getMinTenthPercentage()) {
                passed.add(String.format("10th grade requirement met: %.1f%% (Min required: %.1f%%)", studentTenth, job.getMinTenthPercentage()));
            } else {
                failed.add(String.format("10th grade score too low: %.1f%% (Min required: %.1f%%)", studentTenth, job.getMinTenthPercentage()));
            }
        }

        // 3. 12th / Diploma Percentage Criterion
        if (job.getMinTwelfthPercentage() != null) {
            double studentTwelfth = student.getTwelfthPercentage() != null ? student.getTwelfthPercentage() : 0.0;
            if (studentTwelfth >= job.getMinTwelfthPercentage()) {
                passed.add(String.format("12th/Diploma requirement met: %.1f%% (Min required: %.1f%%)", studentTwelfth, job.getMinTwelfthPercentage()));
            } else {
                failed.add(String.format("12th/Diploma score too low: %.1f%% (Min required: %.1f%%)", studentTwelfth, job.getMinTwelfthPercentage()));
            }
        }

        // 4. Backlogs Criterion
        if (job.getMaxBacklogsAllowed() != null) {
            int studentBacklogs = student.getActiveBacklogs() != null ? student.getActiveBacklogs() : 0;
            if (studentBacklogs <= job.getMaxBacklogsAllowed()) {
                passed.add(String.format("Active backlogs count within limit: %d (Max allowed: %d)", studentBacklogs, job.getMaxBacklogsAllowed()));
            } else {
                failed.add(String.format("Too many active backlogs: %d (Max allowed: %d)", studentBacklogs, job.getMaxBacklogsAllowed()));
            }
        }

        // 5. Allowed Branches Criterion
        if (job.getAllowedBranches() != null && !job.getAllowedBranches().isBlank()) {
            String allowed = job.getAllowedBranches().trim();
            if (allowed.equalsIgnoreCase("ALL")) {
                passed.add("Eligible for all academic branches.");
            } else {
                String studentBranch = student.getBranch() != null ? student.getBranch().trim().toUpperCase() : "";
                List<String> branchList = Arrays.stream(allowed.split(","))
                        .map(b -> b.trim().toUpperCase())
                        .toList();

                if (branchList.contains(studentBranch)) {
                    passed.add(String.format("Branch '%s' is eligible.", studentBranch));
                } else {
                    failed.add(String.format("Branch '%s' is not in allowed list (%s).", studentBranch, job.getAllowedBranches()));
                }
            }
        }

        // 6. Passing Year Criterion
        if (job.getTargetPassingYear() != null) {
            int studentYear = student.getPassingYear() != null ? student.getPassingYear() : 0;
            if (studentYear == job.getTargetPassingYear().intValue()) {
                passed.add(String.format("Graduation batch matches: %d", studentYear));
            } else {
                failed.add(String.format("Graduation batch mismatch: %d (Target: %d)", studentYear, job.getTargetPassingYear()));
            }
        }

        boolean isEligible = failed.isEmpty();
        String summary = isEligible ? "Eligible to apply!" : "Not eligible due to unmet criteria.";

        return EligibilityCheckResponse.builder()
                .eligible(isEligible)
                .passedCriteria(passed)
                .failedCriteria(failed)
                .summary(summary)
                .build();
    }
}
