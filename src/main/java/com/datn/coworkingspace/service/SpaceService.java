package com.datn.coworkingspace.service;

import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.dto.SpaceDTO;
import com.datn.coworkingspace.entity.Space;
import com.datn.coworkingspace.exception.ResourceNotFoundException;
import com.datn.coworkingspace.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional
public class SpaceService implements ISpaceService {

    @Autowired
    private SpaceRepository spaceRepository;


    @Autowired
    private CategoryService categoryService;

    @Override
    public List<Space> findAll() {
        return null;
    }

    @Override
    public Page<Space> findAllPageAndSort(Pageable pagingSort) {
        return null;
    }

    @Override
    public Space findById(Long theId) throws ResourceNotFoundException {
        Optional<Space> space = spaceRepository.findById(theId);
        if(!space.isPresent()) {
            throw  new ResourceNotFoundException("Not found space with ID=" + theId);
        } else {
            space.get().setCategoryIds(space.get().getCategory().getId());

            return space.get();
        }
    }

    @Override
    public MessageResponse createSpace(SpaceDTO theSpaceDto) {
        return null;
    }

    @Override
    public MessageResponse updateSpace(Long theId, SpaceDTO theSpaceDto) {
        return null;
    }

    @Override
    public void deleteSpace(Long theId) {

    }

    @Override
    public Page<Space> findByNameContaining(String spaceName, Pageable pagingSort) {
        return null;
    }

    @Override
    public Long count() {
        return null;
    }

    @Override
    public Long countSpacesByCategoryId(Long theCategoryId) {
        return null;
    }
}


