package com.smartplacement.controller;

import com.smartplacement.dto.ApiResponse;
import com.smartplacement.dto.ApplicationRequest;
import com.smartplacement.dto.ApplicationStatusUpdateRequest;
import com.smartplacement.entity.Application;
import com.smartplacement.security.UserPrincipal;
import com.smartplacement.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<Application>> applyForJob(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody ApplicationRequest request
    ) {
        Application application = applicationService.applyForJob(userPrincipal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Application submitted successfully!", application));
    }

    @GetMapping("/my-applications")
    public ResponseEntity<ApiResponse<List<Application>>> getMyApplications(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        List<Application> applications = applicationService.getApplicationsByStudent(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(applications));
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('ROLE_OFFICER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<Application>>> getApplicationsByJobPosting(@PathVariable Long jobId) {
        List<Application> applications = applicationService.getApplicationsByJobPosting(jobId);
        return ResponseEntity.ok(ApiResponse.success(applications));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_OFFICER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<Application>>> getAllApplications() {
        List<Application> applications = applicationService.getAllApplications();
        return ResponseEntity.ok(ApiResponse.success(applications));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Application>> getApplicationById(@PathVariable Long id) {
        Application application = applicationService.getApplicationById(id);
        return ResponseEntity.ok(ApiResponse.success(application));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_OFFICER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Application>> updateApplicationStatus(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationStatusUpdateRequest request
    ) {
        Application updatedApp = applicationService.updateApplicationStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success("Application status updated", updatedApp));
    }

    @PostMapping("/{id}/respond-offer")
    public ResponseEntity<ApiResponse<Application>> respondToOffer(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam boolean accept
    ) {
        Application updatedApp = applicationService.respondToOffer(id, userPrincipal.getId(), accept);
        String msg = accept ? "Congratulations on accepting the offer!" : "Offer declined.";
        return ResponseEntity.ok(ApiResponse.success(msg, updatedApp));
    }
}
