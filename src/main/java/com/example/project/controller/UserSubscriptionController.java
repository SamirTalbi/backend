package com.example.project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.project.model.UserModel;
import com.example.project.model.UserSubscriptionModel;
import com.example.project.service.UserService;
import com.example.project.service.UserSubscriptionService;

import java.util.Optional;

@RestController
@RequestMapping("/api/user-subscriptions")
@CrossOrigin(origins = "*")
public class UserSubscriptionController {

    @Autowired
    private UserSubscriptionService userSubscriptionService;

    @Autowired
    private UserService userService;

    @GetMapping("/active/{userId}")
    public Optional<UserSubscriptionModel> getActive(@PathVariable Long userId) {
        UserModel user = userService.findById(userId).orElseThrow();
        return userSubscriptionService.getActiveSubscription(user);
    }

    @PostMapping
    public UserSubscriptionModel subscribe(@RequestBody UserSubscriptionModel sub) {
        return userSubscriptionService.save(sub);
    }
}
