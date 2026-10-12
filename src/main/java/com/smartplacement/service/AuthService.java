package com.smartplacement.service;

import com.smartplacement.dto.JwtResponse;
import com.smartplacement.dto.LoginRequest;
import com.smartplacement.dto.RegisterRequest;
import com.smartplacement.entity.Role;
import com.smartplacement.entity.StudentProfile;
import com.smartplacement.entity.User;
import com.smartplacement.enums.RoleType;
import com.smartplacement.exception.BadRequestException;
import com.smartplacement.repository.RoleRepository;
import com.smartplacement.repository.StudentProfileRepository;
import com.smartplacement.repository.UserRepository;
import com.smartplacement.security.JwtTokenProvider;
import com.smartplacement.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Long studentProfileId = null;
        String rollNumber = null;
        String branch = null;

        if (roles.contains(RoleType.ROLE_STUDENT.name())) {
            var profileOpt = studentProfileRepository.findByUserId(userDetails.getId());
            if (profileOpt.isPresent()) {
                StudentProfile profile = profileOpt.get();
                studentProfileId = profile.getId();
                rollNumber = profile.getRollNumber();
                branch = profile.getBranch();
            }
        }

        return JwtResponse.builder()
                .token(jwt)
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .fullName(userDetails.getFullName())
                .roles(roles)
                .studentProfileId(studentProfileId)
                .rollNumber(rollNumber)
                .branch(branch)
                .build();
    }

    @Transactional
    public JwtResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Email is already taken: " + registerRequest.getEmail());
        }

        RoleType roleType = registerRequest.getRole() != null ? registerRequest.getRole() : RoleType.ROLE_STUDENT;
        Role role = roleRepository.findByName(roleType)
                .orElseGet(() -> roleRepository.save(new Role(roleType)));

        User user = User.builder()
                .fullName(registerRequest.getFullName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phone(registerRequest.getPhone())
                .enabled(true)
                .roles(Collections.singleton(role))
                .build();

        User savedUser = userRepository.save(user);

        Long studentProfileId = null;
        String rollNumber = registerRequest.getRollNumber();
        String branch = registerRequest.getBranch();

        if (roleType == RoleType.ROLE_STUDENT) {
            StudentProfile profile = StudentProfile.builder()
                    .user(savedUser)
                    .rollNumber(rollNumber != null ? rollNumber : "STU" + savedUser.getId())
                    .branch(branch != null ? branch : "CSE")
                    .degree(registerRequest.getDegree() != null ? registerRequest.getDegree() : "B.Tech")
                    .passingYear(registerRequest.getPassingYear() != null ? registerRequest.getPassingYear() : 2025)
                    .cgpa(registerRequest.getCgpa() != null ? registerRequest.getCgpa() : 7.0)
                    .tenthPercentage(75.0)
                    .twelfthPercentage(75.0)
                    .activeBacklogs(0)
                    .totalBacklogs(0)
                    .isPlaced(false)
                    .build();

            StudentProfile savedProfile = studentProfileRepository.save(profile);
            studentProfileId = savedProfile.getId();
            rollNumber = savedProfile.getRollNumber();
            branch = savedProfile.getBranch();
        }

        // Auto authenticate after registration
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPassword())
        );
        String jwt = tokenProvider.generateToken(authentication);

        return JwtResponse.builder()
                .token(jwt)
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .roles(Collections.singletonList(roleType.name()))
                .studentProfileId(studentProfileId)
                .rollNumber(rollNumber)
                .branch(branch)
                .build();
    }
}
