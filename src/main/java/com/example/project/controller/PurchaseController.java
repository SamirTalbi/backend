package com.example.project.controller;

import com.example.project.model.EbookModel;
import com.example.project.model.PurchaseModel;
import com.example.project.model.UserModel;
import com.example.project.service.EbookService;
import com.example.project.service.PurchaseService;
import com.example.project.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@CrossOrigin(origins = "http://localhost:4200")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private UserService userService;

    @Autowired
    private EbookService ebookService;

    @GetMapping("/user/{userId}")
    public List<PurchaseModel> getByUser(@PathVariable Long userId) {
        UserModel user = userService.findById(userId).orElseThrow();
        return purchaseService.findByUser(user);
    }


    @PostMapping
    public PurchaseModel purchase(@RequestBody PurchaseModel purchase) {
        UserModel user = userService.findById(purchase.getUser().getId()).orElseThrow();
        EbookModel ebook = ebookService.findById(purchase.getEbook().getId()).orElseThrow();

        if (purchaseService.hasUserPurchasedEbook(user, ebook)) {
            throw new RuntimeException("L'utilisateur a déjà acheté cet ebook.");
        }

        purchase.setUser(user);
        purchase.setEbook(ebook);

        return purchaseService.save(purchase);
    }
}
