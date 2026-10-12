package com.smartplacement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PlacementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlacementSystemApplication.class, args);
        System.out.println("=================================================================");
        System.out.println(" Smart Student Placement & Internship Management System Started! ");
        System.out.println(" Backend API is ready at http://localhost:8080/                   ");
        System.out.println(" H2 Console (if using dev profile): http://localhost:8080/h2-console ");
        System.out.println("=================================================================");
    }
}
