package com.example.Job_Hunter.controller;

import com.example.Job_Hunter.domain.Entity.Skill;
import com.example.Job_Hunter.domain.request.UpdateSkillReq;
import com.example.Job_Hunter.domain.response.ResultPaginationDTO;
import com.example.Job_Hunter.service.SkillService;
import com.example.Job_Hunter.utill.exception.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    public ResponseEntity<Skill> addSkill(@RequestBody Skill skill) throws IdInvalidException {
        return ResponseEntity.ok().body(skillService.createSkill(skill));
    }
    @GetMapping("/skills")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(@Filter Specification<Skill> spec, Pageable pageable) {
        return ResponseEntity.ok(skillService.getSkills(spec, pageable));
    }
    @GetMapping("/skills/{id}")
    public ResponseEntity<Skill> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(skillService.getSkillId(id));
    }
    @DeleteMapping("/skills/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        skillService.deleteSkill(id);
        return ResponseEntity.ok().body("Delete OK");
    }
    @PutMapping("/skills")
    public ResponseEntity<Skill> updateCompany(@RequestBody UpdateSkillReq req) throws IdInvalidException {
        return ResponseEntity.ok(skillService.updateSkill(req));
    }
}
