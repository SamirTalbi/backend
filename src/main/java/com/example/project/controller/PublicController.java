package com.example.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PublicController {

    // Endpoint racine
    @GetMapping("/")
    public Map<String, String> root() {
        return Map.of(
            "service", "backend",
            "status", "running"
        );
    }

    // Endpoint de test public
    @GetMapping("/api/ping")
    public Map<String, String> ping() {
        return Map.of(
            "message", "pong",
            "status", "ok"
        );
    }
}
