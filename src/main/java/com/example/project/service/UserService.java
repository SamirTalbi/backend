package com.example.project.service;


import com.example.project.model.UserModel;
import com.example.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserModel save(UserModel user) {
        return userRepository.save(user);
    }

    public Optional<UserModel> findById(Long id) {
        return userRepository.findById(id);
    }
}
