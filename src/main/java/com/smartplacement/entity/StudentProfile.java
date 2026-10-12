package com.smartplacement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private User user;

    @Column(unique = true, length = 50)
    private String rollNumber;

    @Column(length = 50)
    private String branch; // e.g. CSE, IT, ECE, EEE, MECH, CIVIL

    @Column(length = 50)
    private String degree; // e.g. B.Tech, M.Tech, MCA, BCA

    private Integer passingYear;

    private Double cgpa; // out of 10.0

    private Double tenthPercentage;

    private Double twelfthPercentage;

    @Builder.Default
    private Integer activeBacklogs = 0;

    @Builder.Default
    private Integer totalBacklogs = 0;

    @Column(length = 500)
    private String skills; // comma separated, e.g. "Java, Spring Boot, React, SQL"

    @Column(length = 255)
    private String resumeUrl;

    @Column(length = 255)
    private String resumeOriginalFileName;

    @Column(length = 255)
    private String githubUrl;

    @Column(length = 255)
    private String linkedinUrl;

    @Column(length = 255)
    private String portfolioUrl;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Builder.Default
    private Boolean isPlaced = false;

    @Column(length = 150)
    private String placedCompany;

    private Double packageLpa;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
