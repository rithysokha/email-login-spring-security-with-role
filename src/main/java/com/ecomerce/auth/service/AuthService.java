package com.ecomerce.auth.service;

import com.ecomerce.auth.config.PasswordEncoderConfiguration;
import com.ecomerce.auth.dto.ApiResponseDTO;
import com.ecomerce.auth.dto.RegisterDTO;
import com.ecomerce.auth.model.Role;
import com.ecomerce.auth.model.Users;
import com.ecomerce.auth.repository.AuthRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoderConfiguration passwordEncoder;

    public ResponseEntity<ApiResponseDTO<Users>> register(RegisterDTO registerDTO, Role role) {
        if(authRepository.findByEmail(registerDTO.getEmail()).isPresent()){
            return new ResponseEntity<>(new ApiResponseDTO<>("Email already exists", null), HttpStatus.BAD_REQUEST);
        }
        registerDTO.setPassword(passwordEncoder.passwordEncoder().encode(registerDTO.getPassword()));
        registerDTO.setRole(role);
        Users users = authRepository.save(registerDTO.toUser());
        return new ResponseEntity<>(new ApiResponseDTO<>("User registered successfully", users), HttpStatus.OK);
    }
}
