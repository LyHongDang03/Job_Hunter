package com.example.Job_Hunter.domain.Entity;

import com.example.Job_Hunter.utill.SecurityUtil;
import com.example.Job_Hunter.utill.constant.GenderEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private int age;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private String address;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Resume> jobs;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

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
