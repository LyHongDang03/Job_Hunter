package com.example.Job_Hunter.service;

import com.example.Job_Hunter.domain.Entity.Company;
import com.example.Job_Hunter.domain.Entity.User;
import com.example.Job_Hunter.domain.response.ResultPaginationDTO;
import com.example.Job_Hunter.repository.CompanyRepository;
import com.example.Job_Hunter.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }
    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }
    public ResultPaginationDTO getAllCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageUsers = companyRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageUsers.getTotalPages());
        meta.setTotal(pageUsers.getTotalElements());
        rs.setMeta(meta);
        rs.setResult(pageUsers.getContent());
        return rs;
    }
    public Company getCompanyById(Long id) {
        Optional<Company> company = companyRepository.findById(id);
        return company.orElse(null);
    }
    public Company updateCompany(Company company) {
        Company oldCompany = getCompanyById(company.getId());
        if (oldCompany != null) {
            oldCompany.setName(company.getName());
            oldCompany.setDescription(company.getDescription());
            oldCompany.setAddress(company.getAddress());
            oldCompany.setLogo(company.getLogo());
            return companyRepository.save(oldCompany);
        }
        return null;
    }
    public void deleteCompany(Long id) {
        Optional<Company> company = companyRepository.findById(id);
        if (company.isPresent()) {
            Company com = company.get();
            List<User> users = userRepository.findByCompany(com);
            userRepository.deleteAll(users);
        }
        companyRepository.deleteById(id);
    }
    public Optional<Company> findById(long id) {
        return companyRepository.findById(id);
    }
}
