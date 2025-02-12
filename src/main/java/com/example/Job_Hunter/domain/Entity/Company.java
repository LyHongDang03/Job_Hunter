package com.example.Job_Hunter.domain.Entity;

import com.example.Job_Hunter.utill.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "companies")
@Getter
@Setter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    private String address;
    private String logo;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @JsonIgnore
    List<User> users;
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Job> jobs;

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
