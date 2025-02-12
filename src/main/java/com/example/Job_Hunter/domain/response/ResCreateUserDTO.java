package com.example.Job_Hunter.domain.response;

import com.example.Job_Hunter.utill.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResCreateUserDTO {
    private Long id;
    private String username;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    private String refreshToken;
    private Instant createdAt;
    private String createdBy;
    private CompanyUserDTO company;
    @Getter
    @Setter
    public static class CompanyUserDTO {
        private Long id;
        private String name;
    }
}
