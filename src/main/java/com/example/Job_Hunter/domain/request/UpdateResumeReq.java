package com.example.Job_Hunter.domain.request;

import com.example.Job_Hunter.utill.constant.ResumeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResumeReq {
    private Long resumeId;
    private ResumeEnum status;
}
