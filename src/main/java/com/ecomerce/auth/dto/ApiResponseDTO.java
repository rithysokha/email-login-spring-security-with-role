package com.ecomerce.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseDTO<T> {
    private String message;
    private T Data;

    public ApiResponseDTO(String message, T data) {
        this.message = message;
        Data = data;
    }
}
