package com.smartplacement.controller;

import com.smartplacement.dto.ApiResponse;
import com.smartplacement.dto.StudentProfileRequest;
import com.smartplacement.entity.StudentProfile;
import com.smartplacement.security.UserPrincipal;
import com.smartplacement.service.StudentProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<StudentProfile>> getCurrentStudentProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        StudentProfile profile = studentProfileService.getProfileByUserId(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<StudentProfile>> updateCurrentStudentProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody StudentProfileRequest request
    ) {
        StudentProfile updatedProfile = studentProfileService.updateProfile(userPrincipal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", updatedProfile));
    }

    @PostMapping(value = "/profile/resume", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<StudentProfile>> uploadResume(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam("file") MultipartFile file
    ) {
        StudentProfile profile = studentProfileService.uploadResume(userPrincipal.getId(), file);
        return ResponseEntity.ok(ApiResponse.success("Resume uploaded successfully", profile));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_OFFICER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<StudentProfile>>> getAllStudents() {
        List<StudentProfile> students = studentProfileService.getAllStudents();
        return ResponseEntity.ok(ApiResponse.success(students));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_OFFICER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<StudentProfile>> getStudentById(@PathVariable Long id) {
        StudentProfile student = studentProfileService.getProfileById(id);
        return ResponseEntity.ok(ApiResponse.success(student));
    }
}
