package com.example.Job_Hunter.domain.response;

import com.example.Job_Hunter.utill.constant.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResUpdateUserDTO {
    private Long id;
    private String username;
    private int age;
    private GenderEnum gender;
    private String address;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private CompanyUserDTO company;
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompanyUserDTO {
        private Long id;
        private String name;
    }
}
