package com.example.project.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_subscriptions")
public class UserSubscriptionModel {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserModel user;

    @ManyToOne
    private SubscriptionTypeModel subscription;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private boolean active;

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserModel getUser() { return user; }
    public void setUser(UserModel user) { this.user = user; }

    public SubscriptionTypeModel getSubscription() { return subscription; }
    public void setSubscription(SubscriptionTypeModel subscription) { this.subscription = subscription; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

