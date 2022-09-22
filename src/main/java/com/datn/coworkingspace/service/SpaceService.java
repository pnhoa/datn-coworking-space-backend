package com.datn.coworkingspace.service;

import com.datn.coworkingspace.dto.*;
import com.datn.coworkingspace.entity.*;
import com.datn.coworkingspace.entity.Package;
import com.datn.coworkingspace.exception.ResourceNotFoundException;
import com.datn.coworkingspace.mapper.SpaceMapper;
import com.datn.coworkingspace.repository.SpaceRepository;
import com.datn.coworkingspace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.util.CollectionUtils;

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
    @Transactional
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
        space.setCreatedBy(user.get().getName());
        space.setCreatedDate(new Date());

        if(theSpaceDto.getSpaceDescriptionDTO() != null) {
            SpaceDescription spaceDescription = mapper.map(theSpaceDto.getSpaceDescriptionDTO(), SpaceDescription.class);
            spaceDescription.setSpace(space);
            space.setSpaceDescription(spaceDescription);
        }

        if(theSpaceDto.getSpaceContactDTO() != null) {
            SpaceContact spaceContact = mapper.map(theSpaceDto.getSpaceContactDTO(), SpaceContact.class);
            spaceContact.setSpace(space);
            space.setSpaceContact(spaceContact);
        }

        if(theSpaceDto.getSpaceAmenityDTO() != null) {
            SpaceAmenity spaceAmenity = mapper.map(theSpaceDto.getSpaceAmenityDTO(), SpaceAmenity.class);
            spaceAmenity.setSpace(space);
            space.setSpaceAmenity(spaceAmenity);
        }

        if(theSpaceDto.getSpaceAddressDTO() != null) {
            SpaceAddressDTO spaceAddressDTO = theSpaceDto.getSpaceAddressDTO();

            SpaceAddress spaceAddress = SpaceMapper.getSpaceAddress(spaceAddressDTO);
            spaceAddress.setSpace(space);
            space.setSpaceAddress(spaceAddress);
        }

        if(!CollectionUtils.isEmpty(theSpaceDto.getSpaceOperationTimeDTOs())) {
            Set<SpaceOperationTime> spaceOperationTimes = new HashSet<>();
            for(SpaceOperationTimeDTO spaceOperationTimeDTO : theSpaceDto.getSpaceOperationTimeDTOs()) {
                SpaceOperationTime spaceOperationTime = mapper.map(spaceOperationTimeDTO, SpaceOperationTime.class);

                spaceOperationTimes.add(spaceOperationTime);
            }
            space.setSpaceOperationTimes(spaceOperationTimes);
        }

        if(!CollectionUtils.isEmpty(theSpaceDto.getServiceSpaceDTOs())) {
            Set<ServiceSpace> serviceSpaces = new HashSet<>();
            for(ServiceSpaceDTO serviceSpaceDTO : theSpaceDto.getServiceSpaceDTOs()) {
                ServiceSpace serviceSpace = new ServiceSpace();

                serviceSpace.setTitle(serviceSpaceDTO.getTitle());
                serviceSpace.setNote(serviceSpaceDTO.getNote());

                if(!CollectionUtils.isEmpty(serviceSpaceDTO.getPackageDTOs())) {
                    Set<Package> packages = new HashSet<>();
                    for(PackageDTO packageDTO : serviceSpaceDTO.getPackageDTOs()) {
                        Package packageService = new Package();

                        packageService.setType(packageDTO.getType());
                        packageService.setNote(packageDTO.getNote());

                        if(!CollectionUtils.isEmpty(packageDTO.getSubSpaceDTOs())) {
                            Set<SubSpace> subSpaces = new HashSet<>();
                            for(SubSpaceDTO subSpaceDTO : packageDTO.getSubSpaceDTOs()) {
                                SubSpace subSpace = new SubSpace();

                                subSpace.setTitle(subSpaceDTO.getTitle());
                                subSpace.setPrice(subSpaceDTO.getPrice());
                                subSpace.setImageUrl(subSpaceDTO.getImageUrl());
                                subSpace.setNumberOfPeople(subSpaceDTO.getNumberOfPeople());
                                subSpace.setStatus(subSpaceDTO.isStatus());
                                subSpace.setPackageSubSpace(packageService);

                                subSpaces.add(subSpace);
                            }

                        packageService.setSubSpaces(subSpaces);
                        packageService.setServiceSpace(serviceSpace);

                        packages.add(packageService);
                        }

                    }
                serviceSpace.setPackages(packages);
                serviceSpace.setSpace(space);

                serviceSpaces.add(serviceSpace);
                }

            }

            space.setServiceSpaces(serviceSpaces);
        }

        if(!CollectionUtils.isEmpty(theSpaceDto.getImageUrls())) {
            List<Image> images = new ArrayList<>();
            for(String imageUrl : theSpaceDto.getImageUrls()) {
                Image image = new Image();
                image.setFileName(imageUrl);
                image.setUrl(imageUrl);
                image.setSpace(space);

                images.add(image);
            }
            space.setImages(images);
        }

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


