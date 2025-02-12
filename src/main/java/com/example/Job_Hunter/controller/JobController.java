package com.example.Job_Hunter.controller;

import com.example.Job_Hunter.domain.Entity.Job;
import com.example.Job_Hunter.domain.response.ResCreateJob;
import com.example.Job_Hunter.domain.response.ResUpdateJob;
import com.example.Job_Hunter.domain.response.ResultPaginationDTO;
import com.example.Job_Hunter.service.JobService;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    public final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
    @PostMapping("/jobs")
    public ResponseEntity<ResCreateJob> createJob(@RequestBody Job job) {
        return ResponseEntity.ok().body(jobService.createJob(job));
    }
    @PutMapping("/jobs")
    public ResponseEntity<ResUpdateJob> updateJob(@RequestBody Job job) {
        return ResponseEntity.ok().body(jobService.updateJob(job));
    }
    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok().body("Job with id " + id + " deleted.");
    }
    @GetMapping("/jobs")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(@Filter Specification<Job> spec, Pageable pageable) {
        return ResponseEntity.ok(jobService.getALlJobs(spec, pageable));
    }
    @GetMapping("/jobs/{id}")
    public ResponseEntity<Job> getJob(@PathVariable Long id) {
        return ResponseEntity.ok().body(jobService.getJobById(id));
    }
}
