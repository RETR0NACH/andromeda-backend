package com.andromda.shop.repository;

import com.andromda.shop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Ejemplo de método personalizado (Spring Data JPA lo implementa automágicamente)
    List<Product> findByCategoria(String categoria);
}
