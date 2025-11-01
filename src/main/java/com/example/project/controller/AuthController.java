package com.example.project.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.project.model.Role;
import com.example.project.model.UserModel;
import com.example.project.model.VerificationTokenModel;
import com.example.project.security.JwtUtil;
import com.example.project.service.EmailService;
import com.example.project.service.UserService;
import com.example.project.service.VerificationService;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationService verificationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserModel user) {
        // 1) validations simples
        if (user.getEmail() == null || user.getEmail().isBlank()
                || user.getPassword() == null || user.getPassword().isBlank()
                || user.getFullName() == null || user.getFullName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Champs requis manquants (email, password, fullName)."));
        }

        // 2) email unique
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "EMAIL_TAKEN"));
        }

        // 3) créer l'utilisateur
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setEmailVerified(false); // en DEV tu peux passer à true si tu veux bypass la vérification
        UserModel saved = userService.save(user);

        // 4) créer le token de vérif + tentative d'envoi d'email, sans casser l’inscription
        try {
            verificationService.createToken(saved);
            Optional<VerificationTokenModel> tokenOpt = verificationService.getTokenByUser(saved);

            if (tokenOpt.isPresent()) {
                String token = tokenOpt.get().getToken();
                // TODO: mets ça dans application.properties (base URL front)
                String url = "https://yramus.com/verify?token=" + token;

                emailService.sendEmail(
                        saved.getEmail(),
                        "Confirme ton adresse email",
                        "Clique ici pour activer ton compte : " + url
                );
            }
        } catch (Exception e) {
            // NE PAS échouer l’inscription si SMTP down
            // log si tu as un logger: log.error("Email send failed", e);
            System.err.println("Email send failed: " + e.getMessage());
        }

        // 5) renvoyer un DTO (pas l’entité avec password hash)
        Map<String, Object> dto = Map.of(
                "id", saved.getId(),
                "email", saved.getEmail(),
                "fullName", saved.getFullName(),
                "role", saved.getRole().toString(),
                "emailVerified", saved.isEmailVerified()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody UserModel credentials) {
        UserModel user = userService.findByEmail(credentials.getEmail())
                .orElseThrow(() -> new RuntimeException("Email non trouvé"));

        if (!passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mot de passe invalide");
        }

        // 👉 décommente pour forcer la vérif email avant login
        // if (!user.isEmailVerified()) { throw new RuntimeException("Email non vérifié"); }

        String token = jwtUtil.generateToken(user);

        return Map.of(
                "token", token,
                "email", user.getEmail(),
                "role", user.getRole().toString(),
                "userId", user.getId().toString(),
                "fullName", user.getFullName()
        );
    }


}
