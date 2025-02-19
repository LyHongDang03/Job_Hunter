package com.example.Job_Hunter.service;

import com.example.Job_Hunter.domain.Entity.Job;
import com.example.Job_Hunter.domain.Entity.Skill;
import com.example.Job_Hunter.domain.Entity.Subscriber;
import com.example.Job_Hunter.domain.response.ResEmailJob;
import com.example.Job_Hunter.repository.JobRepository;
import com.example.Job_Hunter.repository.SkillRepository;
import com.example.Job_Hunter.repository.SubscriberRepository;
import com.example.Job_Hunter.utill.exception.IdInvalidException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;
    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository, JobRepository jobRepository, EmailService emailService) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
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
    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {
                         List<ResEmailJob> arr = listJobs.stream().map(
                         job-> this.convertJobToSendEmail(job)).collect(Collectors.toList());
                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "test",
                                sub.getName(),
                                listJobs);
                    }
                }
            }
        }
    }

    public ResEmailJob convertJobToSendEmail(Job job) {
        ResEmailJob res = new ResEmailJob();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new ResEmailJob.CompanyEmail(job.getCompany().getName()));
        List<Skill> skills = job.getSkills();
        List<ResEmailJob.SkillEmail> s = skills.stream().map(skill-> new
                        ResEmailJob.SkillEmail(skill.getName()))
                .collect(Collectors.toList());
        res.setSkills(s);
        return res;
    }
}
