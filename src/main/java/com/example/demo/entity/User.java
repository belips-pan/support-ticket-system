package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role; // "ROLE_ADMIN" ή "ROLE_USER"
    private String fullName;
    private String email;
    private String productOwnership;
    private double balance;
    private String phone;

    @OneToMany(mappedBy = "sender")
    private List<Ticket> tickets;

    // Getters & Setters (NetBeans: Alt+Insert -> Getter and Setter)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getProductOwnership() { return productOwnership; }
    public void setProductOwnership(String productOwnership) { this.productOwnership = productOwnership; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public String getPhone() { return phone;  }
    public void setPhone(String phone) { this.phone = phone;  }
    
}