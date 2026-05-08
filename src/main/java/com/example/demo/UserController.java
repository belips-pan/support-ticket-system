/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo;

/**
 *
 * @author <Panagiotis Beligiannis at HOU>
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.repository.UserRepository;
import com.example.demo.entity.User;


@Controller
@RequestMapping("/admin/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Εμφάνιση όλων των χρηστών
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-list";
    }
  
    @PostMapping("/admin/users/{id}/add-balance")
    public String addBalance(@PathVariable Long id, @RequestParam("amount") Double amount) {
        User user = userRepository.findById(id).orElseThrow();
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
        return "redirect:/admin/users";
    }
    
    
    // Προσθήκη χρημάτων στο υπόλοιπο (Top-up)
    @PostMapping("/{id}/add-balance")
    public String addBalance(@PathVariable Long id, @RequestParam double amount) {
        User user = userRepository.findById(id).orElseThrow();
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
        return "redirect:/admin/users";
    }
}