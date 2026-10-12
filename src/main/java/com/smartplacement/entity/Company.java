package com.smartplacement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 100)
    private String industry;

    @Column(length = 255)
    private String website;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String contactPerson;

    @Column(length = 100)
    private String contactEmail;

    @Column(length = 20)
    private String contactPhone;

    @Column(length = 255)
    private String address;

    @Column(length = 255)
    private String logoUrl;

    @Column(length = 50)
    private String tier; // e.g. Dream, Super Dream, Core, Mass

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
