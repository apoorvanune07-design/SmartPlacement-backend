package com.smartplacement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequest {

    @NotBlank(message = "Company name is required")
    private String name;

    private String industry;
    private String website;
    private String description;
    private String contactPerson;
    private String contactEmail;
    private String contactPhone;
    private String address;
    private String logoUrl;
    private String tier;
}
