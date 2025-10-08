package com.example.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.model.UserModel;
import com.example.project.model.UserSubscriptionModel;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscriptionModel, Long> {
    Optional<UserSubscriptionModel> findByUserAndActiveTrue(UserModel user);
    List<UserSubscriptionModel> findByUser(UserModel user);
}