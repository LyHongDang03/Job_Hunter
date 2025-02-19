package com.example.Job_Hunter.domain.response;

import com.example.Job_Hunter.domain.Entity.Company;
import com.example.Job_Hunter.utill.constant.Level;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ResCreateJob {
    private Long id;
    private String name;
    private Double salary;
    private Integer quantity;
    private Level level;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active;
    private List<String> skill;
    private String company;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
