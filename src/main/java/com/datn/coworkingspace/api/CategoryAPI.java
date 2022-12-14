package com.datn.coworkingspace.api;

import com.datn.coworkingspace.dto.CategoryDTO;
import com.datn.coworkingspace.dto.EmployeeDTO;
import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.entity.Category;
import com.datn.coworkingspace.service.ICategoryService;
import com.datn.coworkingspace.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin
public class CategoryAPI {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<List<Category>> getAll(@RequestParam(name = "q", required = false) String categoryName,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int limit,
                                                 @RequestParam(defaultValue = "id,ASC") String[] sort){

        try {

            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);
            Page<Category> categoryPage;

            if(categoryName == null) {
                categoryPage = categoryService.findAllPageAndSort(pagingSort);
            } else {
                categoryPage = categoryService.findByNameContaining(categoryName, pagingSort);
            }


            return new ResponseEntity<>(categoryPage.getContent(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable("id") Long theId){
        Category theCategory = categoryService.findById(theId);

        return new ResponseEntity<>(theCategory, HttpStatus.OK);
    }

    @PostMapping(value = "",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCategory(@Valid @RequestPart(value = "theCategoryDto") CategoryDTO theCategoryDto,
                                                          @RequestPart(value = "file", required = false) MultipartFile file, BindingResult theBindingResult){

        if(theBindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for create category", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        if(categoryService.existsByName(theCategoryDto.getName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Category name is already use.", HttpStatus.BAD_REQUEST, LocalDateTime.now()));
        }

        MessageResponse messageResponse = categoryService.createCategory(theCategoryDto, file);
        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long theId,
                                                          @Valid @RequestPart(value = "theCategoryDto") CategoryDTO theCategoryDto,
                                                          @RequestPart(value = "file", required = false) MultipartFile file, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for update category", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = categoryService.updateCategory(theId, theCategoryDto, file);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long theId){

        MessageResponse messageResponse = categoryService.deleteCategory(theId);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

}
