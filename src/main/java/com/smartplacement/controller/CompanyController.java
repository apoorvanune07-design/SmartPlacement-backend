package com.smartplacement.controller;

import com.smartplacement.dto.ApiResponse;
import com.smartplacement.dto.CompanyRequest;
import com.smartplacement.entity.Company;
import com.smartplacement.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Company>>> getAllCompanies() {
        return ResponseEntity.ok(ApiResponse.success(companyService.getAllCompanies()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Company>> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(companyService.getCompanyById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_OFFICER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Company>> createCompany(@Valid @RequestBody CompanyRequest request) {
        Company company = companyService.createCompany(request);
        return ResponseEntity.ok(ApiResponse.success("Company registered successfully", company));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_OFFICER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Company>> updateCompany(
            @PathVariable Long id,
            @Valid @RequestBody CompanyRequest request
    ) {
        Company updatedCompany = companyService.updateCompany(id, request);
        return ResponseEntity.ok(ApiResponse.success("Company updated successfully", updatedCompany));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_OFFICER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok(ApiResponse.success("Company deleted successfully", null));
    }
}
