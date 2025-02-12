package com.example.Job_Hunter.controller;

import com.example.Job_Hunter.domain.Entity.Resume;
import com.example.Job_Hunter.domain.request.CreateResumeReq;
import com.example.Job_Hunter.domain.request.UpdateResumeReq;
import com.example.Job_Hunter.domain.response.ResCreateResume;
import com.example.Job_Hunter.domain.response.ResGetResume;
import com.example.Job_Hunter.domain.response.ResUpdateResume;
import com.example.Job_Hunter.domain.response.ResultPaginationDTO;
import com.example.Job_Hunter.service.ResumeService;
import com.example.Job_Hunter.utill.exception.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }
    @PostMapping("/resumes")
    public ResponseEntity<ResCreateResume> createResume(@RequestBody CreateResumeReq request) {
        return ResponseEntity.ok()
                .body(resumeService.createResume(request));
    }
    @GetMapping("resumes/{id}")
    public ResponseEntity<ResGetResume> getResume(@PathVariable("id") Long id) throws IdInvalidException {
        return ResponseEntity.ok().body(resumeService.getResumeById(id));
    }
    @GetMapping("/resumes")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(@Filter Specification<Resume> spec, Pageable pageable) {
        return ResponseEntity.ok(resumeService.getResumes(spec, pageable));
    }
    @PutMapping("/resumes")
    public ResponseEntity<ResUpdateResume> updateJob(@RequestBody UpdateResumeReq req) {
        return ResponseEntity.ok().body(resumeService.updateResume(req));
    }
    @DeleteMapping("/resume/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        resumeService.deleteResume(id);
        return ResponseEntity.ok().body("Delete OK");
    }
}
