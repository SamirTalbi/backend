package com.example.project.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "purchases")
public class PurchaseModel {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserModel user;

    @ManyToOne
    @JsonIgnoreProperties({"purchases"})
    private EbookModel ebook;

    private LocalDateTime purchaseDate = LocalDateTime.now();


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserModel getUser() { return user; }
    public void setUser(UserModel user) { this.user = user; }

    public EbookModel getEbook() { return ebook; }
    public void setEbook(EbookModel ebook) { this.ebook = ebook; }

    public LocalDateTime getPurchaseDate() { return purchaseDate; }
    
    public void setPurchaseDate(LocalDateTime purchaseDate) { this.purchaseDate = purchaseDate; }
}
