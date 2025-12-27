package com.example.jpa.repository;

import com.example.jpa.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    List<Product> findByStockGreaterThan(Integer stock);
    
    // JPQL Query Example
    @Query("SELECT p FROM Product p WHERE p.price > :minPrice AND p.stock > 0")
    List<Product> findAvailableProductsAbovePrice(@Param("minPrice") BigDecimal minPrice);
    
    // JPQL Query with multiple conditions
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword%")
    List<Product> searchProducts(@Param("keyword") String keyword);
    
    // Native Query Example
    @Query(value = "SELECT * FROM products WHERE stock > :minStock ORDER BY price ASC", nativeQuery = true)
    List<Product> findProductsWithStockAbove(@Param("minStock") Integer minStock);
}

