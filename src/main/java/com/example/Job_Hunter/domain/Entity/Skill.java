package com.example.Job_Hunter.domain.Entity;

import com.example.Job_Hunter.utill.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "skills")
@Getter
@Setter
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "skills")
    @JsonIgnore
    private List<Job> jobs;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "skills")
    @JsonIgnore
    private List<Subscriber> subscribers;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin()
                .isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
//        createdBy = SecurityContextHolder.getContext().getAuthentication().getName();
        createdAt = LocalDateTime.now();
    }
    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin()
                .isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
//        createdBy = SecurityContextHolder.getContext().getAuthentication().getName();
        updatedAt = LocalDateTime.now();
    }
}
