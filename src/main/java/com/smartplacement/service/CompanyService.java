package com.smartplacement.service;

import com.smartplacement.dto.CompanyRequest;
import com.smartplacement.entity.Company;
import com.smartplacement.exception.ResourceNotFoundException;
import com.smartplacement.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", id));
    }

    @Transactional
    public Company createCompany(CompanyRequest request) {
        Company company = Company.builder()
                .name(request.getName())
                .industry(request.getIndustry())
                .website(request.getWebsite())
                .description(request.getDescription())
                .contactPerson(request.getContactPerson())
                .contactEmail(request.getContactEmail())
                .contactPhone(request.getContactPhone())
                .address(request.getAddress())
                .logoUrl(request.getLogoUrl())
                .tier(request.getTier() != null ? request.getTier() : "Standard")
                .build();

        return companyRepository.save(company);
    }

    @Transactional
    public Company updateCompany(Long id, CompanyRequest request) {
        Company company = getCompanyById(id);

        company.setName(request.getName());
        company.setIndustry(request.getIndustry());
        company.setWebsite(request.getWebsite());
        company.setDescription(request.getDescription());
        company.setContactPerson(request.getContactPerson());
        company.setContactEmail(request.getContactEmail());
        company.setContactPhone(request.getContactPhone());
        company.setAddress(request.getAddress());
        company.setLogoUrl(request.getLogoUrl());
        if (request.getTier() != null) company.setTier(request.getTier());

        return companyRepository.save(company);
    }

    @Transactional
    public void deleteCompany(Long id) {
        Company company = getCompanyById(id);
        companyRepository.delete(company);
    }
}
