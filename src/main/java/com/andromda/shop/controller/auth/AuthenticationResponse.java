package com.andromda.shop.controller.auth;

import com.andromda.shop.model.Role; // Asegúrate de importar tu Enum Role
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String nombre;
    private String email;
    private Role rol;
}
