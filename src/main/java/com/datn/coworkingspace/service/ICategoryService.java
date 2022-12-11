package com.datn.coworkingspace.service;

import com.datn.coworkingspace.dto.CategoryDTO;
import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {

    List<Category> findAll();

    Category findById(Long theId);

    Optional<Category> findByIdOptional(Long theId);

    MessageResponse createCategory(CategoryDTO theCategoryDto, MultipartFile file);

    MessageResponse updateCategory(Long theId, CategoryDTO theCategoryDto, MultipartFile file);

    MessageResponse deleteCategory(Long theId);

    Page<Category> findAllPageAndSort(Pageable pagingSort);

    Page<Category> findByNameContaining(String categoryName, Pageable pagingSort);

    Boolean existsByName(String name);
}
