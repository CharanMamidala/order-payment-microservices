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
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private StoreRepository storeRepository;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            // Create sample stores
            Store restaurantStore = storeRepository.findByStoreName("Pizza Palace").orElse(null);
            if (restaurantStore == null) {
                restaurantStore = storeRepository.save(new Store("Pizza Palace", "Restaurant", 
                    "Best pizza in town", "123 Main St, City", "+1-555-0100", "restaurant@example.com"));
            }

            Store groceryStore = storeRepository.findByStoreName("Fresh Market").orElse(null);
            if (groceryStore == null) {
                groceryStore = storeRepository.save(new Store("Fresh Market", "Grocery Store", 
                    "Fresh groceries and produce", "456 Oak Ave, City", "+1-555-0200", "grocery@example.com"));
            }

            Store liquorStore = storeRepository.findByStoreName("Beverage World").orElse(null);
            if (liquorStore == null) {
                liquorStore = storeRepository.save(new Store("Beverage World", "Liquor Store", 
                    "Premium beverages and spirits", "789 Pine St, City", "+1-555-0300", "liquor@example.com"));
            }

            // Create SUPER_ADMIN
            userRepository.findByUsername("admin").ifPresentOrElse(
                superAdmin -> {
                    superAdmin.setPassword("admin");
                    superAdmin.setEmail("admin@example.com");
                    superAdmin.setPhoneNumber("+1-555-0000");
                    superAdmin.setUserType("SUPER_ADMIN");
                    superAdmin.setIsActive(true);
                    superAdmin.setStoreId(null); // Super admin not linked to any store
                    userRepository.save(superAdmin);
                },
                () -> userRepository.save(new User(null, "admin", "admin", "SUPER_ADMIN", 
                    "admin@example.com", "+1-555-0000", null, null, null, null, null))
            );

            // Create sample ADMINS (business owners) linked to stores
            User restaurantAdmin = userRepository.findByUsername("restaurant_admin").orElse(null);
            if (restaurantAdmin == null) {
                restaurantAdmin = userRepository.save(new User(null, "restaurant_admin", "restaurant123", "ADMIN", 
                    "restaurant@example.com", "+1-555-0100", "Pizza Palace", "Restaurant", "123 Main St, City", null, restaurantStore.getId()));
            } else {
                restaurantAdmin.setStoreId(restaurantStore.getId());
                userRepository.save(restaurantAdmin);
            }

            User groceryAdmin = userRepository.findByUsername("grocery_admin").orElse(null);
            if (groceryAdmin == null) {
                groceryAdmin = userRepository.save(new User(null, "grocery_admin", "grocery123", "ADMIN", 
                    "grocery@example.com", "+1-555-0200", "Fresh Market", "Grocery Store", "456 Oak Ave, City", null, groceryStore.getId()));
            } else {
                groceryAdmin.setStoreId(groceryStore.getId());
                userRepository.save(groceryAdmin);
            }

            User liquorAdmin = userRepository.findByUsername("liquor_admin").orElse(null);
            if (liquorAdmin == null) {
                liquorAdmin = userRepository.save(new User(null, "liquor_admin", "liquor123", "ADMIN", 
                    "liquor@example.com", "+1-555-0300", "Beverage World", "Liquor Store", "789 Pine St, City", null, liquorStore.getId()));
            } else {
                liquorAdmin.setStoreId(liquorStore.getId());
                userRepository.save(liquorAdmin);
            }

            // Create sample USERS under each admin in their respective stores
            User user1 = userRepository.findByUsername("user1").orElse(null);
            if (user1 == null) {
                user1 = userRepository.save(new User(null, "user1", "user123", "USER", 
                    "user1@example.com", "+1-555-0400", null, null, "100 Customer St, City", restaurantAdmin.getId(), restaurantStore.getId()));
            } else {
                user1.setStoreId(restaurantStore.getId());
                userRepository.save(user1);
            }

            User user2 = userRepository.findByUsername("user2").orElse(null);
            if (user2 == null) {
                user2 = userRepository.save(new User(null, "user2", "user123", "USER", 
                    "user2@example.com", "+1-555-0500", null, null, "200 Customer Ave, City", groceryAdmin.getId(), groceryStore.getId()));
            } else {
                user2.setStoreId(groceryStore.getId());
                userRepository.save(user2);
            }

            User user3 = userRepository.findByUsername("user3").orElse(null);
            if (user3 == null) {
                user3 = userRepository.save(new User(null, "user3", "user123", "USER", 
                    "user3@example.com", "+1-555-0600", null, null, "300 Customer Blvd, City", liquorAdmin.getId(), liquorStore.getId()));
            } else {
                user3.setStoreId(liquorStore.getId());
                userRepository.save(user3);
            }

            // Create demo user for frontend
            User demoUser = userRepository.findByUsername("user").orElse(null);
            if (demoUser == null) {
                demoUser = userRepository.save(new User(null, "user", "user", "USER", 
                    "user@example.com", "+1-555-0700", null, null, "400 Demo St, City", restaurantAdmin.getId(), restaurantStore.getId()));
            } else {
                demoUser.setStoreId(restaurantStore.getId());
                demoUser.setAdminId(restaurantAdmin.getId());
                userRepository.save(demoUser);
            }

            // Create sample products for each store
            // Restaurant products
            if (productRepository.findByAdminId(restaurantAdmin.getId()).isEmpty()) {
                productRepository.save(new Product(null, "Margherita Pizza", "Pizza", new BigDecimal("12.99"), 
                    "https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/72x72/1f355.png", 
                    "Classic tomato and mozzarella pizza.", restaurantAdmin.getId(), restaurantStore.getId(), 50));
                productRepository.save(new Product(null, "Pepperoni Pizza", "Pizza", new BigDecimal("14.99"), 
                    "https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/72x72/1f355.png", 
                    "Spicy pepperoni pizza.", restaurantAdmin.getId(), restaurantStore.getId(), 40));
                productRepository.save(new Product(null, "Caesar Salad", "Salad", new BigDecimal("8.99"), 
                    "https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/72x72/1f957.png", 
                    "Fresh Caesar salad with croutons.", restaurantAdmin.getId(), restaurantStore.getId(), 30));
                productRepository.save(new Product(null, "Garlic Bread", "Appetizer", new BigDecimal("4.99"), 
                    "https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/72x72/1f35e.png", 
                    "Toasted garlic bread.", restaurantAdmin.getId(), restaurantStore.getId(), 60));
            }

            // Grocery products
            if (productRepository.findByAdminId(groceryAdmin.getId()).isEmpty()) {
                productRepository.save(new Product(null, "Organic Bananas", "Fruits", new BigDecimal("2.99"), 
                    "https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/72x72/1f34c.png", 
                    "Fresh organic bananas.", groceryAdmin.getId(), groceryStore.getId(), 100));
                productRepository.save(new Product(null, "Whole Milk", "Dairy", new BigDecimal("3.49"), 
                    "https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/72x72/1f95b.png", 
                    "Fresh whole milk.", groceryAdmin.getId(), groceryStore.getId(), 80));
                productRepository.save(new Product(null, "Whole Wheat Bread", "Bakery", new BigDecimal("2.99"), 
                    "https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/72x72/1f35e.png", 
                    "Fresh whole wheat bread.", groceryAdmin.getId(), groceryStore.getId(), 45));
                productRepository.save(new Product(null, "Organic Eggs", "Dairy", new BigDecimal("4.99"), 
                    "https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/72x72/1f95a.png", 
                    "Farm fresh organic eggs.", groceryAdmin.getId(), groceryStore.getId(), 60));
            }

            // Liquor products
            if (productRepository.findByAdminId(liquorAdmin.getId()).isEmpty()) {
                productRepository.save(new Product(null, "Budweiser", "Beer", new BigDecimal("4.99"), 
                    "https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/72x72/1f37a.png", 
                    "Classic American lager.", liquorAdmin.getId(), liquorStore.getId(), 100));
                productRepository.save(new Product(null, "Jameson", "Whiskey", new BigDecimal("7.99"), 
                    "https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/72x72/1f943.png", 
                    "Famous Irish whiskey.", liquorAdmin.getId(), liquorStore.getId(), 60));
                productRepository.save(new Product(null, "Grey Goose", "Vodka", new BigDecimal("12.99"), 
                    "https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/72x72/1f943.png", 
                    "Premium French vodka.", liquorAdmin.getId(), liquorStore.getId(), 40));
                productRepository.save(new Product(null, "Corona", "Beer", new BigDecimal("5.49"), 
                    "https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/72x72/1f37a.png", 
                    "Mexican pale lager.", liquorAdmin.getId(), liquorStore.getId(), 75));
            }

            System.out.println("Sample data initialized successfully!");
            System.out.println("SUPER_ADMIN: admin / admin");
            System.out.println("DEMO_USER: user / user");
            System.out.println("RESTAURANT_ADMIN: restaurant_admin / restaurant123");
            System.out.println("GROCERY_ADMIN: grocery_admin / grocery123");
            System.out.println("LIQUOR_ADMIN: liquor_admin / liquor123");
            System.out.println("USERS: user1, user2, user3 / user123");
        };
    }
} 