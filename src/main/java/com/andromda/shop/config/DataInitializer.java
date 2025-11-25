package com.andromda.shop.config;

import com.andromda.shop.model.Product;
import com.andromda.shop.model.Role;
import com.andromda.shop.model.Usuario;
import com.andromda.shop.repository.ProductRepository;
import com.andromda.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Crear Usuario ADMIN si no existe
        if (userRepository.findByEmail("admin@andromeda.com").isEmpty()) {
            Usuario admin = Usuario.builder()
                    .nombre("Administrador")
                    .apellido("Principal")
                    .email("admin@andromeda.com")
                    .password(passwordEncoder.encode("admin123"))
                    .rol(Role.ADMIN) //
                    .build();
            userRepository.save(admin);
            System.out.println("✅ Usuario ADMIN creado: admin@andromeda.com / admin123");
        }

        // 2. (Opcional)
        if (productRepository.count() == 0) {
            Product p1 = Product.builder()
                    .nombre("Bong de Vidrio Clásico")
                    .precio(new BigDecimal("45000"))
                    .categoria("Bongs")
                    .descripcion("Bong resistente de 30cm.")
                    .imagen("/images/bong1.png")
                    .build();

            productRepository.save(p1);
            System.out.println("✅ Productos iniciales cargados");
        }
    }
}
