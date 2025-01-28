package com.notanoty.demo.Genrealization.controller;

import java.util.List;
import java.util.Optional;

import com.notanoty.demo.Genrealization.APIResponse.ApiResponse;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;

public interface ControllerBase<T, ID> {
    ResponseEntity<ApiResponse<List<T>>> getAll();

    ResponseEntity<ApiResponse<Optional<T>>> getById(ID id);

    ResponseEntity<ApiResponse<T>> create(T entity);

    ResponseEntity<ApiResponse<T>> update(ID id, T entity);

    ResponseEntity<ApiResponse<Void>> delete(ID id);
}
