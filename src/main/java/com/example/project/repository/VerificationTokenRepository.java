package com.example.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.model.UserModel;
import com.example.project.model.VerificationTokenModel;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationTokenModel, Long> {
    Optional<VerificationTokenModel> findByToken(String token);
    Optional<VerificationTokenModel> findByUser(UserModel user);

}
