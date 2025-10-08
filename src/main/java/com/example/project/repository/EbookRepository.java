package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.model.EbookModel;

@Repository
public interface EbookRepository extends JpaRepository<EbookModel, Long> {
    List<EbookModel> findByTitleContainingIgnoreCase(String title);
}
