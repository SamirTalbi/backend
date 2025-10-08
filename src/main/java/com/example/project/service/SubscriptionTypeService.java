package com.example.project.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.model.SubscriptionTypeModel;
import com.example.project.repository.SubscriptionTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionTypeService {

    @Autowired
    private SubscriptionTypeRepository subscriptionTypeRepository;

    public List<SubscriptionTypeModel> findAll() {
        return subscriptionTypeRepository.findAll();
    }

    public Optional<SubscriptionTypeModel> findById(Long id) {
        return subscriptionTypeRepository.findById(id);
    }

    public SubscriptionTypeModel save(SubscriptionTypeModel type) {
        return subscriptionTypeRepository.save(type);
    }
}
