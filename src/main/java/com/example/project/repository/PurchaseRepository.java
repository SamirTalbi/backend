package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.model.EbookModel;
import com.example.project.model.PurchaseModel;
import com.example.project.model.UserModel;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseModel, Long> {
    List<PurchaseModel> findByUser(UserModel user);
    List<PurchaseModel> findByUserId(Long userId);
    List<PurchaseModel> findByUserAndEbook(UserModel user, EbookModel ebook);
    List<PurchaseModel> findByEbook(EbookModel ebook);

}
