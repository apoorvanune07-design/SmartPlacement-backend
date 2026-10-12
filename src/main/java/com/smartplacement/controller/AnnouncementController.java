package com.smartplacement.controller;

import com.smartplacement.dto.AnnouncementRequest;
import com.smartplacement.dto.ApiResponse;
import com.smartplacement.entity.Announcement;
import com.smartplacement.security.UserPrincipal;
import com.smartplacement.service.AnnouncementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Announcement>>> getAnnouncements() {
        return ResponseEntity.ok(ApiResponse.success(announcementService.getActiveAnnouncements()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_OFFICER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Announcement>> createAnnouncement(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody AnnouncementRequest request
    ) {
        String postedBy = userPrincipal != null ? userPrincipal.getFullName() : "Placement Officer";
        Announcement announcement = announcementService.createAnnouncement(request, postedBy);
        return ResponseEntity.ok(ApiResponse.success("Announcement posted successfully", announcement));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_OFFICER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.ok(ApiResponse.success("Announcement removed", null));
    }
}
