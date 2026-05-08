/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo;

/**
 *
 * @author <Panagiotis Beligiannis at HOU>
 */

import org.springframework.beans.factory.annotation.Autowired; // Για το @Autowired
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List; // Για το List
import com.example.demo.entity.Ticket; // Για το Ticket (αν είναι στο ίδιο package)

@Controller
public class TicketController {

   @Autowired
   private TicketService ticketService;
   
   @GetMapping("/")
    public String index() {
        return "redirect:/login"; 
    }

   /*
    @GetMapping("/")
   public String index(Model model) {
       model.addAttribute("message", "Καλώς ήρθατε στο Support System!");
    
       // Φέρνουμε τη λίστα από το service
       List<Ticket> ticketList = ticketService.getAllTickets();
    
       // Τη στέλνουμε στο HTML με το όνομα "tickets"
       model.addAttribute("tickets", ticketList); 
    
       return "index";
   }
    */
}
