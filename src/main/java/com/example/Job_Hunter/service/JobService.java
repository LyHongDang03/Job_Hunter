package com.example.Job_Hunter.service;

import com.example.Job_Hunter.domain.Entity.Company;
import com.example.Job_Hunter.domain.Entity.Job;
import com.example.Job_Hunter.domain.Entity.Skill;
import com.example.Job_Hunter.domain.response.ResCreateJob;
import com.example.Job_Hunter.domain.response.ResUpdateJob;
import com.example.Job_Hunter.domain.response.ResultPaginationDTO;
import com.example.Job_Hunter.repository.CompanyRepository;
import com.example.Job_Hunter.repository.JobRepository;
import com.example.Job_Hunter.repository.SkillRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository, CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyRepository = companyRepository;
    }
    public ResCreateJob createJob(Job job) {
        if (job != null) {
            List<Long> req = job.getSkills()
                    .stream()
                    .map(Skill::getId)
                    .toList();
            List<Skill> skills = skillRepository.findAllById(req);
            job.setSkills(skills);
        }
        if (job.getCompany() != null) {
            Optional<Company> company = companyRepository.findById(job.getCompany().getId());
            if (company.isPresent()) {
                job.setCompany(company.get());
            }
        }
        assert job != null;
        Job currentJob = jobRepository.save(job);
        ResCreateJob resCreateJob = new ResCreateJob();
        resCreateJob.setId(currentJob.getId());
        resCreateJob.setName(currentJob.getName());
        resCreateJob.setSalary(currentJob.getSalary());
        resCreateJob.setQuantity(currentJob.getQuantity());
        resCreateJob.setDescription(currentJob.getDescription());
        resCreateJob.setStartDate(currentJob.getStartDate());
        resCreateJob.setLevel(currentJob.getLevel());
        resCreateJob.setEndDate(currentJob.getEndDate());
        resCreateJob.setActive(currentJob.isActive());
        resCreateJob.setCreatedAt(currentJob.getCreatedAt());
        resCreateJob.setUpdatedAt(currentJob.getUpdatedAt());
        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills()
                    .stream()
                    .map(Skill::getName)
                    .toList();
            resCreateJob.setSkill(skills);
        }
        if (currentJob.getCompany() != null) {
            String companyName = currentJob.getCompany().getName();
            resCreateJob.setCompany(companyName);
        }
        return resCreateJob;
    }
    public ResUpdateJob updateJob(Job job) {
        if (job != null) {
            List<Long> req = job.getSkills()
                    .stream()
                    .map(Skill::getId)
                    .toList();
            List<Skill> skills = skillRepository.findAllById(req);
            job.setSkills(skills);
        }
        assert job != null;
        Job currentJob = jobRepository.save(job);
        ResUpdateJob resUpdateJob = new ResUpdateJob();
        resUpdateJob.setId(currentJob.getId());
        resUpdateJob.setName(currentJob.getName());
        resUpdateJob.setSalary(currentJob.getSalary());
        resUpdateJob.setQuantity(currentJob.getQuantity());
        resUpdateJob.setDescription(currentJob.getDescription());
        resUpdateJob.setLevel(currentJob.getLevel());
        resUpdateJob.setStartDate(currentJob.getStartDate());
        resUpdateJob.setEndDate(currentJob.getEndDate());
        resUpdateJob.setActive(currentJob.isActive());
        resUpdateJob.setCreatedAt(currentJob.getCreatedAt());
        resUpdateJob.setUpdatedAt(currentJob.getUpdatedAt());
        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills()
                    .stream()
                    .map(Skill::getName)
                    .toList();
            resUpdateJob.setSkill(skills);
        }
        return resUpdateJob;
    }
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }
    public ResultPaginationDTO getALlJobs(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageUsers = jobRepository.findAll(spec, pageable);
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
    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }
}
