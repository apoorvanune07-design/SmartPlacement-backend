package com.smartplacement.controller;

import com.smartplacement.dto.ApiResponse;
import com.smartplacement.dto.JobPostingRequest;
import com.smartplacement.entity.JobPosting;
import com.smartplacement.security.UserPrincipal;
import com.smartplacement.service.JobPostingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobPosting>>> getAllJobs(
            @RequestParam(required = false, defaultValue = "false") Boolean onlyActive
    ) {
        List<JobPosting> jobs = jobPostingService.getAllJobPostings(onlyActive);
        return ResponseEntity.ok(ApiResponse.success(jobs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobPosting>> getJobById(@PathVariable Long id) {
        JobPosting job = jobPostingService.getJobPostingById(id);
        return ResponseEntity.ok(ApiResponse.success(job));
    }

    @GetMapping("/eligible")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getEligibleJobsForStudent(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        List<Map<String, Object>> jobsWithEligibility = jobPostingService.getJobsWithEligibilityForStudent(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(jobsWithEligibility));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_OFFICER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<JobPosting>> createJobPosting(@Valid @RequestBody JobPostingRequest request) {
        JobPosting job = jobPostingService.createJobPosting(request);
        return ResponseEntity.ok(ApiResponse.success("Job posting created successfully", job));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_OFFICER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<JobPosting>> updateJobPosting(
            @PathVariable Long id,
            @Valid @RequestBody JobPostingRequest request
    ) {
        JobPosting updatedJob = jobPostingService.updateJobPosting(id, request);
        return ResponseEntity.ok(ApiResponse.success("Job posting updated successfully", updatedJob));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_OFFICER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteJobPosting(@PathVariable Long id) {
        jobPostingService.deleteJobPosting(id);
        return ResponseEntity.ok(ApiResponse.success("Job posting deleted successfully", null));
    }
}
