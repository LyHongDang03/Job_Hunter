package com.example.Job_Hunter.domain.request;

import com.example.Job_Hunter.domain.Entity.Job;
import com.example.Job_Hunter.domain.Entity.User;
import com.example.Job_Hunter.utill.constant.ResumeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateResumeReq {
    private String email;
    private String url;
    private ResumeEnum status;
    private User user;
    private Job job;

}
