package com.example.project.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.model.EbookModel;
import com.example.project.model.PurchaseModel;
import com.example.project.repository.EbookRepository;
import com.example.project.repository.PurchaseRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EbookService {

    @Autowired
    private EbookRepository ebookRepository;
    
    @Autowired
    private PurchaseRepository purchaseRepository;

    public List<EbookModel> findAll() {
        return ebookRepository.findAll();
    }

    public Optional<EbookModel> findById(Long id) {
        return ebookRepository.findById(id);
    }

    public List<EbookModel> searchByTitle(String title) {
        return ebookRepository.findByTitleContainingIgnoreCase(title);
    }

    public EbookModel save(EbookModel ebook) {
        return ebookRepository.save(ebook);
    }
    public void delete(Long id) {
        EbookModel ebook = ebookRepository.findById(id).orElseThrow();
        List<PurchaseModel> purchases = purchaseRepository.findByEbook(ebook); // 💥 purchaseRepository est null ici
        purchaseRepository.deleteAll(purchases);
        ebookRepository.delete(ebook);
    }


}
