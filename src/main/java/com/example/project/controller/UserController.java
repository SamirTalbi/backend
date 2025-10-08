package com.example.project.controller;

import com.example.project.model.UserModel;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

	 @GetMapping("/me")
	    public UserModel getCurrentUser(Authentication authentication) {
	        return (UserModel) authentication.getPrincipal();
	    }
	}
