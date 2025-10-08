package com.example.project.service;

import com.example.project.model.PurchaseModel;
import com.example.project.model.UserModel;
import com.example.project.model.EbookModel;
import com.example.project.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    public List<PurchaseModel> findByUser(UserModel user) {
        return purchaseRepository.findByUser(user);
    }

    public PurchaseModel save(PurchaseModel purchase) {
        return purchaseRepository.save(purchase);
    }

    public boolean hasUserPurchasedEbook(UserModel user, EbookModel ebook) {
        return !purchaseRepository.findByUserAndEbook(user, ebook).isEmpty();
    }
}
