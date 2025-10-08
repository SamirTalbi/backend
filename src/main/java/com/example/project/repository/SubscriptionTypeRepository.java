package com.example.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.model.SubscriptionTypeModel;

@Repository
public interface SubscriptionTypeRepository extends JpaRepository<SubscriptionTypeModel, Long> {
    Optional<SubscriptionTypeModel> findByName(String name);
}
