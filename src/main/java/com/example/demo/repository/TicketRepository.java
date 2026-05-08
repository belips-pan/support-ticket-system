/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.demo.repository;

/**
 *
 * @author <Panagiotis Beligiannis at HOU>
 */

import com.example.demo.entity.Ticket;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    List<Ticket> findBySender(User user);
    
    // Εδώ το Spring Boot θα φτιάξει μόνο του όλες τις CRUD λειτουργίες!
}
