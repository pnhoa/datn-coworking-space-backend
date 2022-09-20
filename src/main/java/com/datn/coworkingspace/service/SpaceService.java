package com.datn.coworkingspace.service;

import com.datn.coworkingspace.dto.*;
import com.datn.coworkingspace.entity.*;
import com.datn.coworkingspace.entity.Package;
import com.datn.coworkingspace.exception.ResourceNotFoundException;
import com.datn.coworkingspace.repository.SpaceRepository;
import com.datn.coworkingspace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

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

    @Autowired
    private ModelMapper mapper;

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

        SpaceDescription spaceDescription = mapper.map(theSpaceDto.getSpaceDescriptionDTO(), SpaceDescription.class);
        space.setSpaceDescription(spaceDescription);

        SpaceContact spaceContact = mapper.map(theSpaceDto.getSpaceContactDTO(), SpaceContact.class);
        space.setSpaceContact(spaceContact);

        SpaceAmenity spaceAmenity = mapper.map(theSpaceDto.getSpaceAmenityDTO(), SpaceAmenity.class);
        space.setSpaceAmenity(spaceAmenity);

        SpaceAddress spaceAddress = mapper.map(theSpaceDto.getSpaceAddressDTO(), SpaceAddress.class);
        space.setSpaceAddress(spaceAddress);

        Set<SpaceOperationTime> spaceOperationTimes = new HashSet<>();
        for(SpaceOperationTimeDTO spaceOperationTimeDTO : theSpaceDto.getSpaceOperationTimeDTOs()) {
            SpaceOperationTime spaceOperationTime = mapper.map(spaceOperationTimeDTO, SpaceOperationTime.class);

            spaceOperationTimes.add(spaceOperationTime);
        }
        space.setSpaceOperationTimes(spaceOperationTimes);

        Set<ServiceSpace> serviceSpaces = new HashSet<>();
        for(ServiceSpaceDTO serviceSpaceDTO : theSpaceDto.getServiceSpaceDTOS()) {
            ServiceSpace serviceSpace = new ServiceSpace();
            serviceSpace.setTitle(serviceSpaceDTO.getTitle());
            serviceSpace.setNote(serviceSpaceDTO.getNote());

            Set<Package> packages = new HashSet<>();
            for(PackageDTO packageDTO : serviceSpaceDTO.getPackageDTOs()) {
                Package packageService = mapper.map(packageDTO, Package.class);
                packageService.setServiceSpace(serviceSpace);

                Set<SubSpace> subSpaces = new HashSet<>();
                for(SubSpaceDTO subSpaceDTO : packageDTO.getSubSpaceDTOs()) {
                    SubSpace subSpace = mapper.map(subSpaceDTO, SubSpace.class);
                    subSpace.setPackageSubSpace(packageService);

                    subSpaces.add(subSpace);
                }

                packageService.setSubSpaces(subSpaces);
            }
            serviceSpace.setPackages(packages);

            serviceSpaces.add(serviceSpace);
        }

        space.setServiceSpaces(serviceSpaces);

        List<Image> images = new ArrayList<>();
        for(String imageUrl : theSpaceDto.getImageUrls()) {
            Image image = new Image();
            image.setFileName(imageUrl);
            image.setUrl(imageUrl);
            image.setSpace(space);
        }
        space.setImages(images);

        spaceRepository.save(space);


        return new MessageResponse("Create space successfully!", HttpStatus.OK, LocalDateTime.now());
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


