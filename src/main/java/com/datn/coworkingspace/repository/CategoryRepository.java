package com.datn.coworkingspace.repository;

import com.datn.coworkingspace.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

   Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);

   Boolean existsByName(String name);
}
