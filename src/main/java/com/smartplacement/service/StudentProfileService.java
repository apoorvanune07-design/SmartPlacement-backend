package com.smartplacement.service;

import com.smartplacement.dto.StudentProfileRequest;
import com.smartplacement.entity.StudentProfile;
import com.smartplacement.entity.User;
import com.smartplacement.exception.ResourceNotFoundException;
import com.smartplacement.repository.StudentProfileRepository;
import com.smartplacement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public StudentProfile getProfileByUserId(Long userId) {
        return studentProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
                    StudentProfile profile = StudentProfile.builder()
                            .user(user)
                            .rollNumber("STU" + user.getId())
                            .branch("CSE")
                            .degree("B.Tech")
                            .passingYear(2025)
                            .cgpa(7.5)
                            .tenthPercentage(80.0)
                            .twelfthPercentage(80.0)
                            .activeBacklogs(0)
                            .totalBacklogs(0)
                            .isPlaced(false)
                            .build();
                    return studentProfileRepository.save(profile);
                });
    }

    @Transactional(readOnly = true)
    public StudentProfile getProfileById(Long id) {
        return studentProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StudentProfile", "id", id));
    }

    @Transactional(readOnly = true)
    public List<StudentProfile> getAllStudents() {
        return studentProfileRepository.findAll();
    }

    @Transactional
    public StudentProfile updateProfile(Long userId, StudentProfileRequest request) {
        StudentProfile profile = getProfileByUserId(userId);

        if (request.getRollNumber() != null) profile.setRollNumber(request.getRollNumber());
        if (request.getBranch() != null) profile.setBranch(request.getBranch());
        if (request.getDegree() != null) profile.setDegree(request.getDegree());
        if (request.getPassingYear() != null) profile.setPassingYear(request.getPassingYear());
        if (request.getCgpa() != null) profile.setCgpa(request.getCgpa());
        if (request.getTenthPercentage() != null) profile.setTenthPercentage(request.getTenthPercentage());
        if (request.getTwelfthPercentage() != null) profile.setTwelfthPercentage(request.getTwelfthPercentage());
        if (request.getActiveBacklogs() != null) profile.setActiveBacklogs(request.getActiveBacklogs());
        if (request.getTotalBacklogs() != null) profile.setTotalBacklogs(request.getTotalBacklogs());
        if (request.getSkills() != null) profile.setSkills(request.getSkills());
        if (request.getResumeUrl() != null) profile.setResumeUrl(request.getResumeUrl());
        if (request.getGithubUrl() != null) profile.setGithubUrl(request.getGithubUrl());
        if (request.getLinkedinUrl() != null) profile.setLinkedinUrl(request.getLinkedinUrl());
        if (request.getPortfolioUrl() != null) profile.setPortfolioUrl(request.getPortfolioUrl());
        if (request.getBio() != null) profile.setBio(request.getBio());

        return studentProfileRepository.save(profile);
    }

    @Transactional
    public StudentProfile uploadResume(Long userId, MultipartFile file) {
        StudentProfile profile = getProfileByUserId(userId);
        String fileName = fileStorageService.storeFile(file);

        profile.setResumeUrl("/uploads/resumes/" + fileName);
        profile.setResumeOriginalFileName(file.getOriginalFilename());

        return studentProfileRepository.save(profile);
    }
}
