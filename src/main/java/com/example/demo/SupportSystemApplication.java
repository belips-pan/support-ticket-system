/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo;

/**
 *
 * @author <Panagiotis Beligiannis at HOU>
 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SupportSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupportSystemApplication.class, args);
        
        System.out.println("---------------------------------------");
        System.out.println("Το Support System ξεκίνησε επιτυχώς!");
        System.out.println("Πλοηγηθείτε στο: http://localhost:8080");
        System.out.println("---------------------------------------");
    }
}