package com.example.products;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {
    @Autowired
    private UserRepository userRepository;

    @Bean
    CommandLineRunner initDatabase(ProductRepository repository) {
        return args -> {
            repository.save(new Product(null, "Heineken", "Beer", new BigDecimal("4.99"), "https://images.heineken.com/beer.png", "Classic Dutch lager beer."));
            repository.save(new Product(null, "Budweiser", "Beer", new BigDecimal("3.99"), "https://images.budweiser.com/beer.png", "American-style pale lager."));
            repository.save(new Product(null, "Jameson", "Whiskey", new BigDecimal("29.99"), "https://images.jameson.com/whiskey.png", "Famous Irish whiskey."));
            repository.save(new Product(null, "Jack Daniel's", "Whiskey", new BigDecimal("34.99"), "https://images.jackdaniels.com/whiskey.png", "Tennessee whiskey."));
            // Add more products as needed

            // Add a sample admin user
            if (userRepository.findByUsername("admin").isEmpty()) {
                userRepository.save(new User(null, "admin", "admin123", "ADMIN", "admin@example.com", "+1-555-0123"));
            }
            
            // Add a sample regular user
            if (userRepository.findByUsername("user").isEmpty()) {
                userRepository.save(new User(null, "user", "user123", "USER", "user@example.com", "+1-555-0456"));
            }
        };
    }
} 