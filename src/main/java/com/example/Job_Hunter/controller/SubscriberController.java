package com.example.Job_Hunter.controller;

import com.example.Job_Hunter.domain.Entity.Subscriber;
import com.example.Job_Hunter.service.SubscriberService;
import com.example.Job_Hunter.utill.exception.IdInvalidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscriber")
    public ResponseEntity<Subscriber> addSubscriber(@RequestBody Subscriber subscriber) throws IdInvalidException {
        return ResponseEntity.ok().body(subscriberService.createSubscriber(subscriber));
    }
    @PutMapping("/subscriber")
    public ResponseEntity<Subscriber> updateSubscriber(@RequestBody Subscriber subscriber) throws IdInvalidException {
        Subscriber subscriberDb = subscriberService.FindById(subscriber.getId());
        if (subscriberDb == null) {
            throw new IdInvalidException("Subscriber not found");
        }
        return ResponseEntity.ok().body(subscriberService.UpdateSubscriber(subscriberDb, subscriber));
    }

}
