package com.example.Job_Hunter.domain.response;

import com.example.Job_Hunter.utill.constant.ResumeEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResGetResume {
    private long id;
    private String email;
    private String url;
    @Enumerated(EnumType.STRING)
    private ResumeEnum status;
    private UserResponse user;
    private JobResponse job;

    private LocalDateTime createdAt;
    private String createdBy;


    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserResponse {
        private Long id;
        private String email;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class JobResponse {
        private Long id;
        private String name;

    }
}
