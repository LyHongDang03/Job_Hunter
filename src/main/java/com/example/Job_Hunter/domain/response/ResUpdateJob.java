package com.example.Job_Hunter.domain.response;

import com.example.Job_Hunter.utill.constant.Level;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ResUpdateJob {
    private Long id;
    private String name;
    private Double salary;
    private Integer quantity;
    @Enumerated(EnumType.STRING)
    private Level level;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active;
    private List<String> skill;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
