package com.datn.coworkingspace.service;

import com.datn.coworkingspace.dto.CategoryDTO;
import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.entity.Category;
import com.datn.coworkingspace.exception.ResourceNotFoundException;
import com.datn.coworkingspace.repository.CategoryRepository;
import com.datn.coworkingspace.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StorageService storageService;

    @Override
    public List<Category> findAll() {
        List<Category> categories = categoryRepository.findAll();

        return categories;
    }

    @Override
    public Category findById(Long theId) throws ResourceNotFoundException {
        Optional<Category> category = categoryRepository.findById(theId);
        if(!category.isPresent()) {
            throw  new ResourceNotFoundException("Not found category with ID=" + theId);
        } else {
            return category.get();
        }
    }

    @Override
    public Optional<Category> findByIdOptional(Long theId) {
        return categoryRepository.findById(theId);
    }

    @Override
    public MessageResponse createCategory(CategoryDTO theCategoryDto, MultipartFile file) {

        Category theCategory = new Category();

        theCategory.setCreatedDate(new Date());
        theCategory.setCreatedBy(theCategoryDto.getCreatedBy());
        theCategory.setName(theCategoryDto.getName());
        theCategory.setDescription(theCategoryDto.getDescription());
        if(file != null && FileUtils.checkImageFile(file.getOriginalFilename()) ) {
            String thumbnail= storageService.uploadFile(file, FileUtils.generateCategoryUUID());
            theCategory.setThumbnail(thumbnail.replace(" ", ""));
        }

        categoryRepository.save(theCategory);

        return new MessageResponse("Create category successfully!", HttpStatus.CREATED, LocalDateTime.now());
    }

    @Override
    public MessageResponse updateCategory(Long theId, CategoryDTO theCategoryDto, MultipartFile file) {
        Optional<Category> theCategory = categoryRepository.findById(theId);

        if(!theCategory.isPresent()) {
            throw new ResourceNotFoundException("Not found category with ID=" + theId);
        } else {

            if(!theCategory.get().getName().equals(theCategoryDto.getName())){
                if(categoryRepository.existsByName(theCategoryDto.getName())){
                    return new MessageResponse("Error: Category name is already use.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
                }
            }

            theCategory.get().setModifiedDate(new Date());
            theCategory.get().setModifiedBy(theCategoryDto.getModifiedBy());
            theCategory.get().setName(theCategoryDto.getName());
            theCategory.get().setDescription(theCategoryDto.getDescription());
            if(file != null && FileUtils.checkImageFile(file.getOriginalFilename()) ) {
                String thumbnail= storageService.uploadFile(file, FileUtils.generateCategoryUUID());
                theCategory.get().setThumbnail(thumbnail.replace(" ", ""));
            }

            categoryRepository.save(theCategory.get());
        }

        return new MessageResponse("Updated category successfully!", HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public MessageResponse deleteCategory(Long theId) {
        Category theCategory = categoryRepository.findById(theId).orElseThrow(
                () -> new ResourceNotFoundException("Not found category with ID=" + theId));

        if(categoryRepository.count() > 0) {
            return new MessageResponse("Can't delete category, just delete all space in this category", HttpStatus.BAD_REQUEST,
                    LocalDateTime.now());
        }
        categoryRepository.delete(theCategory);
        return new MessageResponse("Deleted successfully!", HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public Page<Category> findAllPageAndSort(Pageable pagingSort) {
        Page<Category> categoryPage =  categoryRepository.findAll(pagingSort);

        return  categoryPage;
    }

    @Override
    public Page<Category> findByNameContaining(String categoryName, Pageable pagingSort) {
        Page<Category> categoryPage =  categoryRepository.findByNameContainingIgnoreCase(categoryName, pagingSort);

        return  categoryPage;
    }

    @Override
    public Boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
}
