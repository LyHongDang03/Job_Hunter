package com.example.Job_Hunter.service;

import com.example.Job_Hunter.domain.Entity.Skill;
import com.example.Job_Hunter.domain.Entity.Subscriber;
import com.example.Job_Hunter.repository.SkillRepository;
import com.example.Job_Hunter.repository.SubscriberRepository;
import com.example.Job_Hunter.utill.exception.IdInvalidException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
    }
    public Subscriber createSubscriber(Subscriber subscriber) throws IdInvalidException {
        if (subscriberRepository.existsByEmail(subscriber.getEmail())) {
            throw new IdInvalidException("Email ton tai");
        }
        if (subscriber.getSkills() != null) {
            List<Long> skills = subscriber.getSkills().stream()
                    .map(Skill::getId)
                    .toList();
            List<Skill> skillList = skillRepository.findAllById(skills);
            subscriber.setSkills(skillList);
        }
        return subscriberRepository.save(subscriber);
    }
    public Subscriber FindById(Long id){
        return subscriberRepository.findById(id).orElse(null);
    }
    public Subscriber UpdateSubscriber(Subscriber subDb, Subscriber subscriber) throws IdInvalidException {
        if (subscriber.getSkills() != null) {
            List<Long> skills = subscriber.getSkills().stream()
                    .map(Skill::getId)
                    .toList();
            List<Skill> skillList = skillRepository.findAllById(skills);
            subscriber.setSkills(skillList);
        }
        return subscriberRepository.save(subDb);
    }
}
