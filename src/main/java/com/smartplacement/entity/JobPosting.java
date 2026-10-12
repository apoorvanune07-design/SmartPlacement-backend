package com.smartplacement.entity;

import com.smartplacement.enums.DriveStatus;
import com.smartplacement.enums.JobType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_postings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private JobType jobType = JobType.FULL_TIME;

    @Column(length = 100)
    private String role; // e.g. Software Engineer, Data Analyst, Cloud Associate

    @Column(length = 100)
    private String location; // e.g. Bangalore, Hyderabad, Remote

    private Double ctcLpa; // e.g. 12.5

    private Double stipendPerMonth; // e.g. 35000.0 for internships

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    // --- Eligibility Criteria ---
    @Builder.Default
    private Double minCgpa = 6.0;

    @Builder.Default
    private Double minTenthPercentage = 60.0;

    @Builder.Default
    private Double minTwelfthPercentage = 60.0;

    @Builder.Default
    private Integer maxBacklogsAllowed = 0;

    @Column(length = 255)
    private String allowedBranches; // comma separated: "CSE,IT,ECE" or "ALL"

    private Integer targetPassingYear; // e.g. 2025

    @Column(length = 255)
    private String rounds; // e.g. "Online Assessment -> Technical Interview -> HR Round"

    private LocalDate applicationDeadline;

    private LocalDate driveDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private DriveStatus driveStatus = DriveStatus.ACTIVE;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
