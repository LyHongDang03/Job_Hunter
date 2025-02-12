package com.example.Job_Hunter.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResCreateResume {
    private Long id;
    private LocalDateTime createdAt;
    private String createdBy;
}
