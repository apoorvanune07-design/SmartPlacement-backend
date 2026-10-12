package com.smartplacement.entity;

import com.smartplacement.enums.InterviewMode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "interview_schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(nullable = false, length = 100)
    private String roundName; // e.g. Technical Round 1, Managerial Round, HR Round

    @Column(nullable = false)
    private LocalDate interviewDate;

    @Column(nullable = false)
    private LocalTime interviewTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private InterviewMode mode = InterviewMode.ONLINE;

    @Column(length = 255)
    private String meetingLinkOrVenue;

    @Column(length = 100)
    private String interviewerName;

    @Column(length = 100)
    private String interviewerEmail;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(length = 30)
    @Builder.Default
    private String status = "SCHEDULED"; // SCHEDULED, COMPLETED, CANCELLED

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
