package com.example.Job_Hunter.controller;

import com.example.Job_Hunter.domain.Entity.Company;
import com.example.Job_Hunter.domain.response.ResultPaginationDTO;
import com.example.Job_Hunter.service.CompanyService;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }
    @PostMapping("/companies")
    public ResponseEntity<?> createCompany(@RequestBody Company company) {
        return ResponseEntity.ok(companyService.createCompany(company));
    }
    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(@Filter Specification<Company> spec, Pageable pageable) {
        return ResponseEntity.ok(companyService.getAllCompanies(spec, pageable));
    }
    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }
    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company company) {
        return ResponseEntity.ok(companyService.updateCompany(company));
    }
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok().body("Delete OK");
    }
}
