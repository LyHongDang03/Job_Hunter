package com.example.Job_Hunter.service;

import com.example.Job_Hunter.domain.Entity.Job;
import com.example.Job_Hunter.domain.Entity.Resume;
import com.example.Job_Hunter.domain.Entity.User;
import com.example.Job_Hunter.domain.request.CreateResumeReq;
import com.example.Job_Hunter.domain.request.UpdateResumeReq;
import com.example.Job_Hunter.domain.response.ResCreateResume;
import com.example.Job_Hunter.domain.response.ResGetResume;
import com.example.Job_Hunter.domain.response.ResUpdateResume;
import com.example.Job_Hunter.domain.response.ResultPaginationDTO;
import com.example.Job_Hunter.repository.JobRepository;
import com.example.Job_Hunter.repository.ResumeRepository;
import com.example.Job_Hunter.repository.UserRepository;
import com.example.Job_Hunter.utill.SecurityUtil;
import com.example.Job_Hunter.utill.exception.IdInvalidException;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.converter.FilterStringConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final FilterParser filterParser;
    private final FilterSpecificationConverter filterSpecificationConverter;

    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository, JobRepository jobRepository, FilterParser filterParser, FilterStringConverter filterStringConverter, FilterSpecificationConverter filterSpecificationConverter) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.filterParser = filterParser;
        this.filterSpecificationConverter = filterSpecificationConverter;
    }


    public ResCreateResume createResume(CreateResumeReq req) {
        Resume resume = new Resume();
        Optional<User> userId = userRepository.findById(req.getUser().getId());
        Optional<Job> jobId = jobRepository.findById(req.getJob().getId());
        resume.setEmail(req.getEmail());
        resume.setUrl(req.getUrl());
        resume.setStatus(req.getStatus());
        resume.setUser(userId.orElse(null));
        resume.setJob(jobId.orElse(null));
        resumeRepository.save(resume);
        ResCreateResume resCreateResume = new ResCreateResume();
        resCreateResume.setId(resume.getId());
        resCreateResume.setCreatedAt(resume.getCreatedAt());
        resCreateResume.setCreatedBy(resume.getCreatedBy());
        return resCreateResume;
    }
    public ResGetResume getResumeById(Long id) throws IdInvalidException {
        Resume resumeEntity = resumeRepository.findById(id).orElse(null);
        if (resumeEntity == null) {
            throw new IdInvalidException("Resume with id " + id + " not found");
        }
        ResGetResume resGetResume = new ResGetResume();
        resGetResume.setId(resumeEntity.getId());
        resGetResume.setEmail(resumeEntity.getEmail());
        resGetResume.setUrl(resumeEntity.getUrl());
        resGetResume.setStatus(resumeEntity.getStatus());
        resGetResume.setCreatedAt(resumeEntity.getCreatedAt());
        resGetResume.setCreatedBy(resumeEntity.getCreatedBy());
        ResGetResume.UserResponse userresponse = new ResGetResume.UserResponse(resumeEntity.getUser().getId(), resumeEntity.getUser().getEmail());
        resGetResume.setUser(userresponse);
        ResGetResume.JobResponse jobresponse = new ResGetResume.JobResponse(resumeEntity.getJob().getId(), resumeEntity.getJob().getName());
        resGetResume.setJob(jobresponse);
        return resGetResume;
    }
    public ResultPaginationDTO getResumes(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> pageUsers = resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageUsers.getTotalPages());
        meta.setTotal(pageUsers.getTotalElements());
        rs.setMeta(meta);
        List<ResGetResume> resumes = pageUsers.getContent().stream()
                .map(item -> {
                    try {
                        return getResumeById(item.getId());
                    } catch (IdInvalidException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        rs.setResult(resumes);
        return rs;
    }
    public ResUpdateResume updateResume(UpdateResumeReq req) {
        Resume resume = resumeRepository.findById(req.getResumeId()).orElse(null);
        assert resume != null;
        resume.setStatus(req.getStatus());
        resumeRepository.save(resume);
        ResUpdateResume resUpdateResume = new ResUpdateResume();
        resUpdateResume.setId(resume.getId());
        resUpdateResume.setStatus(resume.getStatus());
        resUpdateResume.setUpdatedAt(resume.getUpdatedAt());
        resUpdateResume.setUpdatedBy(resume.getUpdatedBy());
        return resUpdateResume;
    }
    public void deleteResume(Long id) {
        Resume resumeEntity = resumeRepository.findById(id).orElse(null);
        assert resumeEntity != null;
        resumeRepository.delete(resumeEntity);
    }
    public ResultPaginationDTO fetchResumesByUser(Pageable pageable) {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        FilterNode node = filterParser.parse("email ='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pageUsers = resumeRepository.findAll(spec, pageable);
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
}
