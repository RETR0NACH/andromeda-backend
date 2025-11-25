package com.andromda.shop.controller;

import com.andromda.shop.model.Product;
import com.andromda.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    // 1. Obtener todos (Público)
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 2. Obtener por ID (Público)
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Crear Producto (Solo Admin - validado en SecurityConfig)
    @PostMapping
    public Product createProduct(@RequestBody Product producto) {
        return productRepository.save(producto);
    }

    // 4. Actualizar Producto (Solo Admin)
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productoDetails) {
        return productRepository.findById(id)
                .map(producto -> {
                    producto.setNombre(productoDetails.getNombre());
                    producto.setPrecio(productoDetails.getPrecio());
                    producto.setImagen(productoDetails.getImagen());
                    producto.setCategoria(productoDetails.getCategoria());
                    producto.setDescripcion(productoDetails.getDescripcion());
                    return ResponseEntity.ok(productRepository.save(producto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. Eliminar Producto (Solo Admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
