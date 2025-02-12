package com.example.Job_Hunter.repository;

import com.example.Job_Hunter.domain.Entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long>, JpaSpecificationExecutor<Subscriber> {
    boolean existsByEmail(String email);
}
