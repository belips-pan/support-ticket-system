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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    

    @Override
    public void run(String... args) throws Exception {
        // Αν η βάση είναι άδεια, φτιάξε έναν Admin και έναν Customer
        if (userRepository.count() == 0) {
            
            // 1. Δημιουργία ADMIN
            User admin = new User();
            admin.setUsername("admin");
            // Εδώ γίνεται η "μαγεία": Ο κωδικός κρυπτογραφείται πριν αποθηκευτεί
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("System Administrator");
            admin.setEmail("admin@support.com");
            admin.setRole("ROLE_ADMIN");
            admin.setProductOwnership("ALL");
            admin.setBalance(0.0);
            userRepository.save(admin);

            // 2. Δημιουργία CUSTOMER
            User customer = new User();
            customer.setUsername("user");
            customer.setPassword(passwordEncoder.encode("user123"));
            customer.setFullName("Panagiotis Test");
            customer.setEmail("customer@example.com");
            customer.setRole("ROLE_USER");
            customer.setProductOwnership("PRO_V1");
            customer.setBalance(100.0); // Του δίνουμε και 100 ευρώ για δοκιμές!
            userRepository.save(customer);

            System.out.println("---------------------------------------");
            System.out.println("Δημιουργήθηκαν δοκιμαστικοί χρήστες:");
            System.out.println("Admin: admin / admin123");
            System.out.println("User: user / user123");
            System.out.println("---------------------------------------");
        }
    }
}
