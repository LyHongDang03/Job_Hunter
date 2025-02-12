package com.example.Job_Hunter.domain.response;

import com.example.Job_Hunter.utill.constant.ResumeEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResUpdateResume {
    private Long id;
    private ResumeEnum status;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
