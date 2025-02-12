package com.example.Job_Hunter.domain.Entity;

import com.example.Job_Hunter.utill.SecurityUtil;
import com.example.Job_Hunter.utill.constant.ResumeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "resumes")
@Getter
@Setter
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String url;
    @Enumerated(EnumType.STRING)
    private ResumeEnum status;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin()
                .isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        createdAt = LocalDateTime.now();
    }
    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin()
                .isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        updatedAt = LocalDateTime.now();
    }
}
