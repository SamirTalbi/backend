package com.example.project.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "subscription_ebooks_access")
public class SubscriptionEbookAccessModel {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserModel user;

    @ManyToOne
    private EbookModel ebook;

    private LocalDateTime accessDate = LocalDateTime.now();

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserModel getUser() { return user; }
    public void setUser(UserModel user) { this.user = user; }

    public EbookModel getEbook() { return ebook; }
    public void setEbook(EbookModel ebook) { this.ebook = ebook; }

    public LocalDateTime getAccessDate() { return accessDate; }
    public void setAccessDate(LocalDateTime accessDate) { this.accessDate = accessDate; }
}
