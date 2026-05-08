/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo;

/**
 *
 * @author <Panagiotis Beligiannis at HOU>
 */

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.time.LocalDateTime;

@Controller
public class AppController {

    @Autowired private TicketRepository ticketRepo;
    @Autowired private EmailService emailService;
    @Autowired private UserRepository userRepo;
    @Autowired private TicketService ticketService;
    @Autowired private MessageRepository messageRepo;
    @Autowired
private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    // Εμφάνιση της σελίδας Login
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        // 1. Έλεγχος αν το username υπάρχει ήδη στη βάση
        if (userRepo.findByUsername(user.getUsername()) != null) {
            // Αν υπάρχει, προσθέτουμε ένα μήνυμα λάθους και επιστρέφουμε στη σελίδα εγγραφής
            model.addAttribute("error", "Το όνομα χρήστη χρησιμοποιείται ήδη!");
            return "login"; 
        }

        // 2. Αν το username είναι διαθέσιμο, προχωράμε στην κρυπτογράφηση και τις ρυθμίσεις
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        user.setBalance(100.0); // δώρο 
    
        // 3. Αποθήκευση
        userRepo.save(user);

        // 4. Αποστολή Email 
        try {
            emailService.sendSupportEmail(
                user.getEmail(), 
                "Καλώς ήρθατε", 
                "Ο λογαριασμός σας ενεργοποιήθηκε για το προϊόν: " + user.getProductOwnership()
            );
        } catch (Exception e) {
            System.out.println("Email error: " + e.getMessage());
            // Δεν σταματάμε την εγγραφή αν αποτύχει μόνο το email
        }

    return "redirect:/login?registered";
}

    // Ανακατεύθυνση βάσει ROLE μετά το login (ΕΝΗΜΕΡΩΜΕΝΗ)
    @GetMapping("/default")
    public String defaultAfterLogin(Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/client/dashboard";
    }

    // ADMIN DASHBOARD
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("tickets", ticketRepo.findAll());
        model.addAttribute("users", userRepo.findAll());
        return "admin-dashboard";
    }

    // --- DELETE ---
    @GetMapping("/admin/tickets/delete/{id}")
    public String deleteTicket(@PathVariable Long id) {
        ticketRepo.deleteById(id);
        return "redirect:/admin/dashboard";
    }

    // --- CREATE (POST) ---
    @PostMapping("/admin/tickets/create")
    public String createTicket(@RequestParam String title, 
                           @RequestParam String description, 
                           @RequestParam Long userId) {
        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setStatus("OPEN");
        ticket.setCreatedAt(LocalDateTime.now());
    
        // Σύνδεση με τον χρήστη που επιλέξαμε
        User user = userRepo.findById(userId).orElseThrow();
        ticket.setSender(user);
    
        ticketRepo.save(ticket);
        return "redirect:/admin/dashboard";
    }
    
    @PostMapping("/client/tickets/create")
    public String createTicketClient(@RequestParam String title, 
                                 @RequestParam String description, 
                                 Authentication auth) {
        // 1. Βρίσκουμε ποιος είναι ο συνδεδεμένος χρήστης (Client)
        String username = auth.getName();
        User user = userRepo.findByUsername(username);

        // 2. Δημιουργούμε το ticket
        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setStatus("OPEN");
        ticket.setSender(user); // Αυτόματη σύνδεση με τον Client
        ticket.setCreatedAt(LocalDateTime.now());
    
        // Αντιστοίχιση προϊόντος για να περνάει τους μελλοντικούς ελέγχους
        ticket.setProductCode(user.getProductOwnership());

        ticketRepo.save(ticket);
    
        // 3. Επιστροφή στο Dashboard του Client
        return "redirect:/client/dashboard";
    }
    
    /*
    // CLIENT DASHBOARD
    @GetMapping("/client/dashboard")
    public String clientDashboard(Model model, Authentication auth) {
        User user = userRepo.findByUsername(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("myTickets", ticketRepo.findBySender(user));
        return "client-dashboard";
    }
    */

    // Υποβολή απάντησης από Admin
    @PostMapping("/admin/solve")
    public String solveTicket(@RequestParam Long ticketId, 
                              @RequestParam String response,
                              @RequestParam double amount) {
        ticketService.resolveTicket(ticketId, response, amount);
        return "redirect:/admin/dashboard";
    }
    
    @PostMapping("/admin/tickets/{id}/reply")
    public String replyToTicket(@PathVariable Long id, @RequestParam String content, Authentication auth) {
        Ticket ticket = ticketRepo.findById(id).orElseThrow();
    
        // Δημιουργία νέου μηνύματος
        Message message = new Message(
            content, 
            auth.getName(), // Το όνομα του Admin που απαντά
            "ROLE_ADMIN", 
            ticket
        );
    
        messageRepo.save(message);
    
        // Ενημέρωση κατάστασης του Ticket
        ticket.setStatus("Replied");
        ticketRepo.save(ticket);
    
        return "redirect:/admin/dashboard";
    }
    
    @GetMapping("/admin/tickets/{id}")
    public String viewTicket(@PathVariable Long id, Model model) {
        Ticket ticket = ticketRepo.findById(id).orElseThrow();
    
        // Φορτώνουμε τα μηνύματα με χρονολογική σειρά
        List<Message> chatHistory = messageRepo.findByTicketIdOrderBySentAtAsc(id);
    
        model.addAttribute("ticket", ticket);
        model.addAttribute("chatHistory", chatHistory);
    
        return "ticket-view"; 
    }
    // CLIENT DASHBOARD 
    @GetMapping("/client/dashboard")
    public String clientDashboard(Model model, Authentication auth) {
        // 1. Παίρνουμε το username του συνδεδεμένου χρήστη
        String username = auth.getName();
    
        // 2. Βρίσκουμε τον χρήστη στη βάση
        User user = userRepo.findByUsername(username);
    
        // Έλεγχος ασφαλείας: αν για κάποιο λόγο ο χρήστης δεν υπάρχει
        if (user == null) {
            return "redirect:/login?error";
        }

        // 3. Στέλνουμε τα δεδομένα στο HTML
        model.addAttribute("user", user);
        model.addAttribute("tickets", ticketRepo.findBySender(user));
    
        return "client-dashboard";
    }

    // Προβολή συγκεκριμένου ticket για τον Χρήστη (Chat view)
    @GetMapping("/client/tickets/{id}")
    public String viewClientTicket(@PathVariable Long id, Model model, Authentication auth) {
        Ticket ticket = ticketRepo.findById(id).orElseThrow();
        User user = userRepo.findByUsername(auth.getName());

        // Ασφάλεια: Μην αφήσεις τον User A να δει το ticket του User B
        if (!ticket.getSender().getId().equals(user.getId())) {
            return "redirect:/client/dashboard?error=unauthorized";
        }

        model.addAttribute("ticket", ticket);
        model.addAttribute("chatHistory", messageRepo.findByTicketIdOrderBySentAtAsc(id));
        return "ticket-view"; 
    }
    
    @GetMapping("/admin/reports")
    public String showReports(Model model) {
        List<Ticket> allTickets = ticketRepo.findAll();
        List<User> allUsers = userRepo.findAll();

        double totalRevenue = allTickets.stream()
                .mapToDouble(Ticket::getChargeAmount)
                .sum();

        long openTickets = allTickets.stream()
                .filter(t -> "OPEN".equals(t.getStatus()))
                .count();

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("openTickets", openTickets);
        model.addAttribute("totalTickets", allTickets.size());
        model.addAttribute("users", allUsers);

        return "admin-reports";
    }
}