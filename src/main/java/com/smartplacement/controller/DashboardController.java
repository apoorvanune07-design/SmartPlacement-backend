package com.smartplacement.controller;

import com.smartplacement.dto.ApiResponse;
import com.smartplacement.dto.DashboardStatsResponse;
import com.smartplacement.enums.RoleType;
import com.smartplacement.security.UserPrincipal;
import com.smartplacement.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getDashboardStats(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        boolean isOfficerOrAdmin = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(r -> r.equals(RoleType.ROLE_OFFICER.name()) || r.equals(RoleType.ROLE_ADMIN.name()));

        DashboardStatsResponse stats;
        if (isOfficerOrAdmin) {
            stats = dashboardService.getOfficerDashboardStats();
        } else {
            stats = dashboardService.getStudentDashboardStats(userPrincipal.getId());
        }

        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
