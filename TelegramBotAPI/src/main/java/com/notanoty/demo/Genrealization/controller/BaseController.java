package com.notanoty.demo.Genrealization.controller;

import com.notanoty.demo.Genrealization.APIResponse.ApiResponse;
import com.notanoty.demo.Genrealization.service.ServiceBase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

public abstract class BaseController<T, ID> implements ControllerBase<T, ID> {
    private final ServiceBase<T, ID> service;

    protected BaseController(ServiceBase<T, ID> service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<T>>> getAll() {
        List<T> entities = service.findAll();
        return ApiResponse.success(entities, "Fetched all entities successfully.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Optional<T>>> getById(@PathVariable ID id) {
        Optional<T> entity = service.findById(id);
        if (entity.isPresent()) {
            return ApiResponse.success(entity, "Entity fetched successfully.");
        } else {
            return ApiResponse.error("Entity not found", "Failed to fetch entity.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<T>> create(@RequestBody T entity) {
        T savedEntity = service.save(entity);
        return ApiResponse.success(savedEntity, "Entity created successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<T>> update(@PathVariable ID id, @RequestBody T entity) {
        T updatedEntity = service.update(id, entity);
        return ApiResponse.success(updatedEntity, "Entity updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable ID id) {
        service.deleteById(id);
        return ApiResponse.success(null, "Entity deleted successfully.");
    }

    public ServiceBase<T, ID> getService() {
        return service;
    }
}
