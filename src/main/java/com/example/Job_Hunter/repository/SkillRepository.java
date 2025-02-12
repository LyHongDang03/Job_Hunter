package com.example.Job_Hunter.repository;

import com.example.Job_Hunter.domain.Entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>, JpaSpecificationExecutor<Skill> {
    boolean existsByName(String name);
}
