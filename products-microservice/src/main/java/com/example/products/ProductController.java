package com.example.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id);
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return productRepository.save(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }

    @GetMapping("/top")
    public List<Product> getTopProducts() {
        List<Product> topProducts = new java.util.ArrayList<>();
        // Top 4 beers
        topProducts.add(new Product(null, "Budweiser", "Beer", new java.math.BigDecimal("4.99"), "https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/72x72/1f37a.png", "Classic American lager."));
        topProducts.add(new Product(null, "Miller Lite", "Beer", new java.math.BigDecimal("4.49"), "https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/72x72/1f37a.png", "Light American pilsner."));
        topProducts.add(new Product(null, "Corona", "Beer", new java.math.BigDecimal("5.49"), "https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/72x72/1f37a.png", "Mexican pale lager."));
        topProducts.add(new Product(null, "Heineken", "Beer", new java.math.BigDecimal("5.99"), "https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/72x72/1f37a.png", "Dutch pale lager."));
        // Top 4 whiskeys
        topProducts.add(new Product(null, "Jameson", "Whiskey", new java.math.BigDecimal("7.99"), "https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/72x72/1f943.png", "Famous Irish whiskey."));
        topProducts.add(new Product(null, "Jack Daniel's", "Whiskey", new java.math.BigDecimal("8.99"), "https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/72x72/1f943.png", "Tennessee whiskey."));
        topProducts.add(new Product(null, "Glenfiddich", "Whiskey", new java.math.BigDecimal("14.99"), "https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/72x72/1f943.png", "Single malt Scotch."));
        topProducts.add(new Product(null, "Glenmorangie", "Whiskey", new java.math.BigDecimal("13.99"), "https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/72x72/1f943.png", "Highland single malt Scotch."));
        return topProducts;
    }
} 