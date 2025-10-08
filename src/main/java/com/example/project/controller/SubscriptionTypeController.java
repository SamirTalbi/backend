package com.example.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.project.model.SubscriptionTypeModel;
import com.example.project.service.SubscriptionTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/subscription-types")
@CrossOrigin(origins = "*")
public class SubscriptionTypeController {

    @Autowired
    private SubscriptionTypeService subscriptionTypeService;

    @GetMapping
    public List<SubscriptionTypeModel> getAll() {
        return subscriptionTypeService.findAll();
    }

    @PostMapping
    public SubscriptionTypeModel create(@RequestBody SubscriptionTypeModel type) {
        return subscriptionTypeService.save(type);
    }
}

