package com.datn.coworkingspace.service;

import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.dto.SpaceDTO;
import com.datn.coworkingspace.entity.*;
import com.datn.coworkingspace.exception.ResourceNotFoundException;
import com.datn.coworkingspace.repository.SpaceRepository;
import com.datn.coworkingspace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;


@Service
@Transactional
public class SpaceService implements ISpaceService {

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private UserRepository userRepository;


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
    public MessageResponse createSpace(SpaceDTO theSpaceDto)  {

        Optional<User> user = userRepository.findByIdCustomer(theSpaceDto.getUserId());
        if(!user.isPresent()) {
            return new MessageResponse("Not found customer with ID=" + theSpaceDto.getUserId(), HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        Optional<Category> category = categoryService.findByIdOptional(theSpaceDto.getCategoryId());
        if(!category.isPresent()) {
            return new MessageResponse("Not found category with ID=" + theSpaceDto.getCategoryId(), HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        Space space = new Space();
        space.setUser(user.get());
        space.setCategory(category.get());
        space.setName(theSpaceDto.getName());
        space.setPrice(theSpaceDto.getPrice());
        space.setLargeImage(theSpaceDto.getLargeImage());
        space.setMinPrice(theSpaceDto.getMinPrice());
        space.setMaxPrice(theSpaceDto.getMaxPrice());
        space.setNumberOfRoom(theSpaceDto.getNumberOfRoom());
        space.setAcreage(theSpaceDto.getAcreage());
        space.setElectricPrice(theSpaceDto.getElectricPrice());
        space.setWaterPrice(theSpaceDto.getWaterPrice());
        space.setStatus(false);
        space.setApproved(false);
        space.setxCoordinate(theSpaceDto.getxCoordinate());
        space.setyCoordinate(theSpaceDto.getyCoordinate());
        space.setDiscount(theSpaceDto.getDiscount());
        space.setRatingAverage(BigDecimal.ZERO);

        SpaceDescription spaceDescription = new SpaceDescription();

        space.setSpaceDescription(spaceDescription);

        SpaceContact spaceContact = new SpaceContact();

        space.setSpaceContact(spaceContact);

        SpaceAmenity spaceAmenity = new SpaceAmenity();

        space.setSpaceAmenity(spaceAmenity);

        SpaceAddress spaceAddress = new SpaceAddress();

        space.setSpaceAddress(spaceAddress);

        SpaceOperationTime spaceOperationTime = new SpaceOperationTime();




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


