package com.example.Job_Hunter.domain.response;

import com.example.Job_Hunter.domain.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResLoginDTO {
    private String access_token;
    private UserLogin userLogin;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserLogin{
        private Long id;
        private String email;
        private String name;
        private Role roles;
    }
}
