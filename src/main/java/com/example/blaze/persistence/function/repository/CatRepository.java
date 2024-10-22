package com.example.blaze.persistence.function.repository;

import com.example.blaze.persistence.function.model.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CatRepository extends JpaRepository<Cat, Long>, JpaSpecificationExecutor<Cat> {
}
