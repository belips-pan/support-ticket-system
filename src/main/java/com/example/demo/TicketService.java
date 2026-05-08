/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo;
/**
 *
 * @author <Panagiotis Beligiannis at HOU>
 */

import com.example.demo.entity.Ticket;
import com.example.demo.entity.User;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class TicketService {

    @Autowired private TicketRepository ticketRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;

    @Transactional
    public String resolveTicket(Long ticketId, String response, double amount) {
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        if (ticket == null) return "Ticket not found";

        User user = ticket.getSender();
        ticket.setChargeAmount(amount);

        /* ΠΡΟΣΩΡΙΝΗ ΑΦΑΙΡΕΣΗ ΓΙΑ ΔΟΚΙΜΗ
        if (!user.getProductOwnership().equals(ticket.getProductCode())) {
            return "Σφάλμα: Μη έγκυρη ιδιοκτησία προϊόντος.";
        }
        if (user.getBalance() < ticket.getChargeAmount()) {
            return "Σφάλμα: Ανεπαρκές υπόλοιπο.";
        }
        */

        user.setBalance(user.getBalance() - amount);
        ticket.setAdminResponse(response);
        ticket.setStatus("CLOSED");

        userRepository.save(user);
        ticketRepository.save(ticket);

        return "OK";
    }
    
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
}