/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo;

/**
 *
 * @author <Panagiotis Beligiannis at HOU>
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Απενεργοποίηση CSRF για να δουλεύουν οι POST φόρμες (Solve, Create Ticket, Register)
            .csrf(csrf -> csrf.disable()) 
            
            // 2. Ρύθμιση Δικαιωμάτων Πρόσβασης
            .authorizeHttpRequests(auth -> auth
                // Επιτρέπουμε σε ΟΛΟΥΣ την πρόσβαση στη σελίδα εισόδου, εγγραφής και στα στατικά αρχεία (CSS/JS)
                .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll()
                
                // Περιορισμοί ανά ρόλο
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/client/**").hasRole("USER")
                
                // Οποιοδήποτε άλλο αίτημα απαιτεί απλά να είναι κάποιος συνδεδεμένος
                .requestMatchers("/default", "/").authenticated()
                .anyRequest().permitAll()
            )
            
            // 3. Ρύθμιση Form Login
            .formLogin(form -> form
                .loginPage("/login")           // Η δική μας custom σελίδα
                .loginProcessingUrl("/login")  // Το URL που θα κάνει POST η φόρμα
                .defaultSuccessUrl("/default", true)
                .permitAll()
            )
            
            // 4. Ρύθμιση Logout
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}