/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.repository;

/**
 *
 * @author <Panagiotis Beligiannis at HOU>
 */

import com.example.demo.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    // Εύρεση όλων των μηνυμάτων ενός συγκεκριμένου Ticket με χρονολογική σειρά
    List<Message> findByTicketIdOrderBySentAtAsc(Long ticketId);
}