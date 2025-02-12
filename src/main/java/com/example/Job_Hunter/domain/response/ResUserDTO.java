package com.example.Job_Hunter.domain.response;

import com.example.Job_Hunter.utill.constant.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {
    private Long id;
    private String username;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private CompanyUserDTO companyUser;
    private RoleUserDTO roleUser;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompanyUserDTO {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleUserDTO {
        private Long id;
        private String roleName;
    }
}
