package com.example.Job_Hunter.domain.request;

import com.example.Job_Hunter.domain.Entity.Company;
import com.example.Job_Hunter.utill.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserReq {
    private Long id;
    private String username;
    private String password;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    private Company company;
}
