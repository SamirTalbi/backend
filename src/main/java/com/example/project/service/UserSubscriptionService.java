package com.example.project.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.model.UserModel;
import com.example.project.model.UserSubscriptionModel;
import com.example.project.repository.UserSubscriptionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserSubscriptionService {

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    public Optional<UserSubscriptionModel> getActiveSubscription(UserModel user) {
        return userSubscriptionRepository.findByUserAndActiveTrue(user);
    }

    public List<UserSubscriptionModel> getAllSubscriptions(UserModel user) {
        return userSubscriptionRepository.findByUser(user);
    }

    public UserSubscriptionModel save(UserSubscriptionModel sub) {
        return userSubscriptionRepository.save(sub);
    }
}
