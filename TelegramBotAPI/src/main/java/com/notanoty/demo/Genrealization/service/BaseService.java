package com.notanoty.demo.Genrealization.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.JpaRepository;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public abstract class BaseService<T, ID> implements ServiceBase<T, ID> {
    private final JpaRepository<T, ID> repository;

    protected BaseService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    public List<T> findAll() {
        return repository.findAll();
    };

    public Optional<T> findById(ID id) {
        return repository.findById(id);
    };

    public T save(T entity) {
        return repository.save(entity);
    };

    @Transactional
    public T update(ID id, T entity) {
        T existingEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found with id: " + id));

        copyNonNullProperties(entity, existingEntity);
        return repository.save(existingEntity);
    }

    public void deleteById(ID id) throws RuntimeException {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Entity not found with id: " + id);
        }
        repository.deleteById(id);
    };

    public JpaRepository<T, ID> getRepository() {
        return repository;
    }

    public static <T> void copyNonNullProperties(T source, T target) {
        for (PropertyDescriptor property : BeanUtils.getPropertyDescriptors(source.getClass())) {
            try {
                Method getter = property.getReadMethod();
                Method setter = property.getWriteMethod();
                if (getter != null && setter != null) {
                    Object value = getter.invoke(source);
                    if (value != null) {
                        setter.invoke(target, value);
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

}
