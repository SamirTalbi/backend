package com.example.project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.project.model.EbookModel;
import com.example.project.repository.EbookRepository;
import com.example.project.service.EbookService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/ebooks")
@CrossOrigin(origins = "http://localhost:4200")
public class EbookController {

    @Autowired
    private EbookService ebookService;

   
    
    @GetMapping
    public List<EbookModel> getAll() {
        return ebookService.findAll();
    }

    @GetMapping("/{id}")
    public EbookModel getById(@PathVariable Long id) {
        return ebookService.findById(id).orElseThrow();
    }

    @PostMapping
    public EbookModel create(@RequestBody EbookModel ebook) {
        return ebookService.save(ebook);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            ebookService.delete(id);
            return ResponseEntity.ok("eBook supprimé avec succès.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("eBook introuvable.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la suppression.");
        }
    }



    @GetMapping("/search")
    public List<EbookModel> search(@RequestParam String q) {
        return ebookService.searchByTitle(q);
    }
    @PutMapping("/{id}")
    public EbookModel update(@PathVariable Long id, @RequestBody EbookModel updatedEbook) {
        updatedEbook.setId(id);
        return ebookService.save(updatedEbook); 
    }

}