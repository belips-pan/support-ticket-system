/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.entity;

/**
 *
 * @author <Panagiotis Beligiannis at HOU>
 */

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob // Επιτρέπει μακροσκελή κείμενα (LONGTEXT στη βάση)
    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime sentAt;
    
    private String senderName; // Π.χ. "Admin" ή το όνομα του χρήστη
    private String senderRole; // Για να ξεχωρίζουμε τα χρώματα στο UI (ROLE_ADMIN/ROLE_USER)

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    // Constructors
    public Message() {}
    public Message(String content, String senderName, String senderRole, Ticket ticket) {
        this.content = content;
        this.senderName = senderName;
        this.senderRole = senderRole;
        this.ticket = ticket;
        this.sentAt = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getSentAt() { return sentAt; }
    public String getSenderName() { return senderName; }
    public String getSenderRole() { return senderRole; }
    public Ticket getTicket() { return ticket; }
}