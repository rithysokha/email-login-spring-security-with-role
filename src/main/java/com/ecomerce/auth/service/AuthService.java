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
        String token = getToken(users.getEmail());
        return new ResponseEntity<>(new AuthResponseDTO("User registered successfully", token, token), HttpStatus.OK);
    }

    public ResponseEntity<AuthResponseDTO> login(LoginDTO loginBody) {
       Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginBody.email(), loginBody.password()
        ));
       if(authentication.isAuthenticated()){
           String token = getToken(loginBody.email());
           return new ResponseEntity<>(new AuthResponseDTO("Login successful", token, token), HttpStatus.OK);
       }
       return new ResponseEntity<>(new AuthResponseDTO("Login failed", null, null), HttpStatus.UNAUTHORIZED);
    }

    private String getToken(String users) {
        return jwtService.generateToken(usersDetailService.loadUserByUsername(users));
    }
}
