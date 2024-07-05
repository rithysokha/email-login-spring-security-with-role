package com.ecomerce.auth.service;

import com.ecomerce.auth.config.PasswordEncoderConfiguration;
import com.ecomerce.auth.dto.AuthResponseDTO;
import com.ecomerce.auth.dto.LoginDTO;
import com.ecomerce.auth.dto.RegisterDTO;
import com.ecomerce.auth.model.Role;
import com.ecomerce.auth.model.Users;
import com.ecomerce.auth.repository.UsersRepository;
import com.ecomerce.auth.token.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UsersRepository authRepository;
    private final PasswordEncoderConfiguration passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsersDetailService usersDetailService;

    public ResponseEntity<AuthResponseDTO> register(RegisterDTO registerDTO, Role role) {
        if(authRepository.findByEmail(registerDTO.getEmail()).isPresent()){
            return new ResponseEntity<>(new AuthResponseDTO("Email already exists", null, null), HttpStatus.BAD_REQUEST);
        }
        registerDTO.setPassword(passwordEncoder.passwordEncoder().encode(registerDTO.getPassword()));
        registerDTO.setRole(role);
        Users users = authRepository.save(registerDTO.toUser());
        String accessToken = getToken(users.getEmail(), "access");
        String refreshToken = getToken(users.getEmail(),  "refresh");
        return new ResponseEntity<>(new AuthResponseDTO("User registered successfully", accessToken, refreshToken), HttpStatus.OK);
    }

    public ResponseEntity<AuthResponseDTO> login(LoginDTO loginBody) {
       Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginBody.email(), loginBody.password()
        ));
       if(authentication.isAuthenticated()){
           String accessToken = getToken(loginBody.email(),  "access");
           String refreshToken = getToken(loginBody.email(),  "access");
           return new ResponseEntity<>(new AuthResponseDTO("Login successful", accessToken, refreshToken), HttpStatus.OK);
       }
       return new ResponseEntity<>(new AuthResponseDTO("Login failed", null, null), HttpStatus.UNAUTHORIZED);
    }

    private String getToken(String email, String type) {
        return jwtService.generateToken(usersDetailService.loadUserByUsername(email), type);
    }

    public String refreshToken(String refreshToken) {
       return jwtService.generateToken(usersDetailService.loadUserByUsername(jwtService.extractEmail(refreshToken)), "refresh");
    }
}
