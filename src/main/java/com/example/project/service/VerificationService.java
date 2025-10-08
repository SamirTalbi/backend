package com.example.project.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.model.UserModel;
import com.example.project.model.VerificationTokenModel;
import com.example.project.repository.VerificationTokenRepository;

@Service
public class VerificationService {

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private UserService userService;

    public void createToken(UserModel user) {
        VerificationTokenModel token = new VerificationTokenModel();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpirationDate(LocalDateTime.now().plusDays(1));
        tokenRepository.save(token);
    }

    public boolean verifyToken(String tokenValue) {
        return tokenRepository.findByToken(tokenValue).map(token -> {
            if (token.getExpirationDate().isBefore(LocalDateTime.now())) {
                return false;
            }
            UserModel user = token.getUser();
            user.setEmailVerified(true);
            userService.save(user);
            tokenRepository.delete(token);
            return true;
        }).orElse(false);
    }
    public Optional<VerificationTokenModel> getTokenByUser(UserModel user) {
        return tokenRepository.findAll().stream()
            .filter(t -> t.getUser().getId().equals(user.getId()))
            .findFirst();
    }

}
