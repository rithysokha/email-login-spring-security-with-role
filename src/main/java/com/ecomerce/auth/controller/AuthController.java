package com.ecomerce.auth.controller;

import com.ecomerce.auth.dto.AuthResponseDTO;
import com.ecomerce.auth.dto.LoginDTO;
import com.ecomerce.auth.dto.RegisterDTO;
import com.ecomerce.auth.model.Role;
import com.ecomerce.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterDTO registerDTO) {
        return authService.register(registerDTO, Role.USER);
    }
    @PostMapping("/admin/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponseDTO> registerAdmin(@RequestBody RegisterDTO registerDTO) {
        return authService.register(registerDTO, Role.ADMIN);
    }

    @PostMapping("/refresh")
    public String refreshAuthenticationToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("Refresh-Token");

        return authService.refreshToken(refreshToken);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginBody) {
        return authService.login(loginBody);
    }
}
