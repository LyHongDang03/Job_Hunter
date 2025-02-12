package com.example.Job_Hunter.domain.Entity;

import com.example.Job_Hunter.utill.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "subscribers")
@Getter
@Setter
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "subscribers" })
    @JoinTable(name = "subscriber_skill", joinColumns = @JoinColumn(name =
            "subscriber_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;

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
