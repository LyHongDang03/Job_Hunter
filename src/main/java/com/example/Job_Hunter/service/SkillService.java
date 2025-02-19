package com.example.Job_Hunter.service;

import com.example.Job_Hunter.domain.Entity.Job;
import com.example.Job_Hunter.domain.Entity.Skill;
import com.example.Job_Hunter.domain.request.UpdateSkillReq;
import com.example.Job_Hunter.domain.response.ResultPaginationDTO;
import com.example.Job_Hunter.repository.SkillRepository;
import com.example.Job_Hunter.utill.exception.IdInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SkillService {
    private final SkillRepository skillRepository;
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }
    public Skill createSkill(Skill skill) throws IdInvalidException {
        if (skillRepository.existsByName(skill.getName())) {
            throw new IdInvalidException("Skill "+ skill.getName() + " Đã tồn tại");
        }
        return skillRepository.save(skill);
    }
    public Skill updateSkill(UpdateSkillReq req) throws IdInvalidException {
        if (skillRepository.existsByName(req.getName())) {
            throw new IdInvalidException("Skill "+ req.getName() + " Đã tồn tại");
        }
        Skill sk = skillRepository.findById(req.getId()).orElse(null);
        if (sk != null) {
            sk.setName(req.getName());
        }
        assert sk != null;
        return skillRepository.save(sk);
    }
    public ResultPaginationDTO getSkills(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pageUsers = skillRepository.findAll(spec, pageable);
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
    public Skill getSkillId(Long id) {
        return skillRepository.findById(id).orElse(null);
    }
    public void deleteSkill(Long id) {
        Optional<Skill> skill = skillRepository.findById(id);
        Skill currentSkill = skill.orElse(null);
        assert currentSkill != null;
        for (Job job: currentSkill.getJobs()){
            job.getSkills().remove(currentSkill);
        }
        for (Subscriber sub: currentSkill.getSubscribers()){
            sub.getSkills().remove(currentSkill);
        }
        skillRepository.delete(skill.orElse(null));
    }
}
