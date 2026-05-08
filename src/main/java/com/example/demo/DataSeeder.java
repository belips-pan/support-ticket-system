/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo;

/**
 *
 * @author <Panagiotis Beligiannis at HOU>
 */

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Δημιουργία Admin
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // Κρυπτογράφηση!
            admin.setRole("ROLE_ADMIN");
            admin.setFullName("System Admin");
            admin.setEmail("admin@example.com");
            userRepository.save(admin);

            // Δημιουργία Απλού Χρήστη
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123")); // Κρυπτογράφηση!
            user.setRole("ROLE_USER"); // ή ROLE_CLIENT ανάλογα με τον κώδικά σας
            user.setFullName("Simple User");
            user.setEmail("user@example.com");
            user.setBalance(100.0);
            user.setProductOwnership("PROD-001");
            userRepository.save(user);
            
            System.out.println("Base users created successfully!");
        }
    }
}