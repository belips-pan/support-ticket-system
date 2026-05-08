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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSupportEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("no-reply@supportsystem.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            
            mailSender.send(message);
            System.out.println("Email στάλθηκε επιτυχώς στον: " + to);
        } catch (Exception e) {
            System.err.println("Αποτυχία αποστολής email: " + e.getMessage());
            // Σε περιβάλλον ανάπτυξης, εκτυπώνουμε το σφάλμα αλλά δεν σταματάμε την εφαρμογή
        }
    }
}