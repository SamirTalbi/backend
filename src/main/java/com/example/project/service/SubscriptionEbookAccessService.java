package com.example.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.model.EbookModel;
import com.example.project.model.SubscriptionEbookAccessModel;
import com.example.project.model.UserModel;
import com.example.project.repository.SubscriptionEbookAccessRepository;

import java.util.List;

@Service
public class SubscriptionEbookAccessService {

    @Autowired
    private SubscriptionEbookAccessRepository accessRepository;

    public List<SubscriptionEbookAccessModel> getAccessList(UserModel user) {
        return accessRepository.findByUser(user);
    }

    public boolean hasAccess(UserModel user, EbookModel ebook) {
        return accessRepository.existsByUserAndEbook(user, ebook);
    }

    public SubscriptionEbookAccessModel save(SubscriptionEbookAccessModel access) {
        return accessRepository.save(access);
    }
}
