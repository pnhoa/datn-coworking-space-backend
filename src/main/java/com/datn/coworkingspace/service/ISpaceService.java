package com.datn.coworkingspace.service;

import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.dto.SpaceDTO;
import com.datn.coworkingspace.entity.Space;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ISpaceService {

    List<Space> findAll();

    Page<Space> findAllPageAndSort(Pageable pagingSort);

    Space findById(Long theId);

    MessageResponse createSpace(SpaceDTO theSpaceDto);

    MessageResponse updateSpace(Long theId, SpaceDTO theSpaceDto);

    void deleteSpace(Long theId);

    Page<Space> findByNameContaining(String spaceName, Pageable pagingSort);

    Long count();

    Long countSpacesByCategoryId(Long theCategoryId);

}
