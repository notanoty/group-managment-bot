package com.notanoty.demo.Genrealization.APIResponse;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class ApiResponse<T> {
    private T data;
    private String error;
    private String message;

    public ApiResponse(T data, String error, String message) {
        this.data = data;
        this.error = error;
        this.message = message;
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>(data, null, message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(String errorMessage, String message, HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>(null, errorMessage, message);
        return new ResponseEntity<>(response, status);
    }

    public static <T> ResponseEntity<ApiResponse<T>> makeResponse(T data, String message, HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>(data, null, message);
        return new ResponseEntity<>(response, status);
    }
}
