package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.model.EbookModel;
import com.example.project.model.SubscriptionEbookAccessModel;
import com.example.project.model.UserModel;

@Repository
public interface SubscriptionEbookAccessRepository extends JpaRepository<SubscriptionEbookAccessModel, Long> {
    List<SubscriptionEbookAccessModel> findByUser(UserModel user);
    boolean existsByUserAndEbook(UserModel user, EbookModel ebook);
}
