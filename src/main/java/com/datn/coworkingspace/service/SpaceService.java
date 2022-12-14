package com.datn.coworkingspace.service;

import com.datn.coworkingspace.dto.*;
import com.datn.coworkingspace.entity.*;
import com.datn.coworkingspace.entity.Package;
import com.datn.coworkingspace.enums.BookingStatus;
import com.datn.coworkingspace.exception.ResourceNotFoundException;
import com.datn.coworkingspace.mapper.SpaceMapper;
import com.datn.coworkingspace.repository.*;
import com.datn.coworkingspace.utils.CommonUtils;
import com.datn.coworkingspace.utils.FileUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class SpaceService implements ISpaceService {

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private SpaceDescriptionRepository spaceDescriptionRepository;

    @Autowired
    private SpaceContactRepository spaceContactRepository;

    @Autowired
    private SpaceAmenityRepository spaceAmenityRepository;

    @Autowired
    private SpaceAddressRepository spaceAddressRepository;

    @Autowired
    private SpaceOperationTimeRepository spaceOperationTimeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServicePackRepository servicePackRepository;

    @Autowired
    private  SpacePaymentRepository spacePaymentRepository;

    @Autowired
    private SubSpaceRepository subSpaceRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private StorageService storageService;

    @Override
    public List<Space> findAll() {
        return null;
    }

    @Override
    public Page<Space> findAllPageAndSort(Pageable pagingSort) {
        Page<Space> spacePage =  spaceRepository.findAll(pagingSort);

        return updateDataSpaces(spacePage);
    }

    @Override
    public Space findById(Long theId) throws ResourceNotFoundException {
        Optional<Space> spaceOpt = spaceRepository.findById(theId);
        if(!spaceOpt.isPresent()) {
            throw  new ResourceNotFoundException("Not found space with ID=" + theId);
        } else {
            Space space = spaceOpt.get();
            setDataSpace(space);

            return space;
        }
    }

    @Override
    @Transactional
    public MessageResponse createSpace(SpaceDTO theSpaceDto, MultipartFile largeFile, MultipartFile[] files, MultipartFile[] subSpaceFiles)  {

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
        space.setUnit(theSpaceDto.getUnit());
        String imageUrl = storageService.uploadFile(largeFile, FileUtils.generateSpaceUUID());
        space.setLargeImage(imageUrl.replace(" ", ""));
        space.setMinPrice(theSpaceDto.getMinPrice());
        space.setMaxPrice(theSpaceDto.getMaxPrice());
        space.setNumberOfRoom(theSpaceDto.getNumberOfRoom());
        space.setAcreage(theSpaceDto.getAcreage());
        space.setElectricPrice(theSpaceDto.getElectricPrice());
        space.setWaterPrice(theSpaceDto.getWaterPrice());
        space.setStatus(true);
        space.setApproved(false);
        space.setNotApproved(false);
        space.setPaid(false);
        space.setExpired(true);
        space.setxCoordinate(theSpaceDto.getxCoordinate());
        space.setyCoordinate(theSpaceDto.getyCoordinate());
        space.setDiscount(theSpaceDto.getDiscount());
        space.setRatingAverage(new BigDecimal("5"));
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

                spaceOperationTime.setSpace(space);
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
                                //TODO get name of image by titleService titlePackage titleSubSpace
                                subSpace.setImageUrl(subSpaceDTO.getImageUrl());
                                subSpace.setNumberOfPeople(subSpaceDTO.getNumberOfPeople());
                                subSpace.setStatus(subSpaceDTO.isStatus());
                                subSpace.setPackageSubSpace(packageService);
                                subSpace.setCreatedDate(new Date());
                                subSpace.setCreatedBy(user.get().getName());

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
        List<String> imageUrls = theSpaceDto.getImageUrls();
        if(!CollectionUtils.isEmpty(imageUrls)) {
            List<Image> images = new ArrayList<>();
            for(String imageStr : imageUrls) {
                Image image = new Image();
                image.setFileName(imageStr);
                image.setUrl(imageStr);
                image.setSpace(space);

                images.add(image);
            }
            space.setImages(images);
        }


//        if(files != null && files.length > 0) {
//            List<Image> images = new ArrayList<>();
//            for(MultipartFile file: files) {
//                Image image = new Image();
//                String fileUrl = storageService.uploadFile(file, FileUtils.generateSpaceUUID()).replace(" ", "");
//                image.setFileName(fileUrl);
//                image.setUrl(fileUrl);
//                image.setSpace(space);
//
//                images.add(image);
//            }
//            space.setImages(images);
//        }

        spaceRepository.save(space);

        for(ServiceSpace serviceSpace : space.getServiceSpaces()) {
            for(Package pack : serviceSpace.getPackages()) {
                for(SubSpace subSpace : pack.getSubSpaces()) {
                    subSpace.setSpaceId(space.getId());
                    subSpaceRepository.save(subSpace);
                }
            }
        }


        return new MessageResponse("Create space successfully!", HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public MessageResponse updateSpace(Long theId, SpaceDTO theSpaceDto) {
        Optional<Space> space = spaceRepository.findById(theId);
        if(!space.isPresent()) {
            return new MessageResponse("Not found space with ID=" + theId, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        space.get().setName(theSpaceDto.getName());
        space.get().setPrice(theSpaceDto.getPrice());
        space.get().setUnit(theSpaceDto.getUnit());
        space.get().setLargeImage(theSpaceDto.getLargeImage());
        space.get().setMinPrice(theSpaceDto.getMinPrice());
        space.get().setMaxPrice(theSpaceDto.getMaxPrice());
        space.get().setNumberOfRoom(theSpaceDto.getNumberOfRoom());
        space.get().setAcreage(theSpaceDto.getAcreage());
        space.get().setElectricPrice(theSpaceDto.getElectricPrice());
        space.get().setWaterPrice(theSpaceDto.getWaterPrice());
        space.get().setxCoordinate(theSpaceDto.getxCoordinate());
        space.get().setyCoordinate(theSpaceDto.getyCoordinate());
        space.get().setModifiedBy(space.get().getUser().getName());
        space.get().setModifiedDate(new Date());

        spaceRepository.save(space.get());
        return new MessageResponse("Update space successfully!", HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public MessageResponse updateSpaceDescription(Long id, SpaceDescriptionDTO spaceDescriptionDTO) {
        Optional<SpaceDescription> spaceDescription = spaceDescriptionRepository.findById(id);
        if(!spaceDescription.isPresent()) {
            return new MessageResponse("Not found space description with ID=" + id, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        spaceDescription.get().setOpeningDate(spaceDescriptionDTO.getOpeningDate());
        spaceDescription.get().setShortDescription(spaceDescriptionDTO.getShortDescription());
        spaceDescription.get().setDescription(spaceDescriptionDTO.getDescription());
        spaceDescriptionRepository.save(spaceDescription.get());
        return new MessageResponse("Update space description successfully!", HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public MessageResponse updateSpaceAddress(Long id, SpaceAddressDTO spaceAddressDTO) {
        Optional<SpaceAddress> spaceAddress = spaceAddressRepository.findById(id);
        if(!spaceAddress.isPresent()) {
            return new MessageResponse("Not found space address with ID=" + id, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        spaceAddress.get().setLocationName(spaceAddressDTO.getLocationName());
        spaceAddress.get().setAddressLine1(spaceAddressDTO.getAddressLine1());
        spaceAddress.get().setAddressLine2(spaceAddressDTO.getAddressLine2());
        spaceAddress.get().setFloorNumber(spaceAddressDTO.getFloorNumber());
        spaceAddress.get().setCountry(spaceAddressDTO.getCountry());
        spaceAddress.get().setProvince(spaceAddressDTO.getProvince());
        spaceAddress.get().setDistrict(spaceAddressDTO.getDistrict());
        spaceAddress.get().setSubDistrict(spaceAddressDTO.getSubDistrict());
        spaceAddress.get().setZipCode(spaceAddressDTO.getZipCode());
        spaceAddressRepository.save(spaceAddress.get());

        return new MessageResponse("Update space address successfully!", HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public MessageResponse updateSpaceContact(Long id, SpaceContactDTO spaceContactDTO) {
        Optional<SpaceContact> spaceContact = spaceContactRepository.findById(id);
        if(!spaceContact.isPresent()) {
            return new MessageResponse("Not found space contact with ID=" + id, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        spaceContact.get().setEmail(spaceContactDTO.getEmail());
        spaceContact.get().setPhone(spaceContactDTO.getPhone());
        spaceContact.get().setFacebookUrl(spaceContactDTO.getFacebookUrl());
        spaceContact.get().setInstagramUrl(spaceContactDTO.getInstagramUrl());
        spaceContact.get().setWebsiteUrl(spaceContactDTO.getWebsiteUrl());
        spaceContactRepository.save(spaceContact.get());

        return new MessageResponse("Update space contact successfully!", HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public MessageResponse updateSpaceAmenity(Long id, SpaceAmenityDTO spaceAmenityDTO) {
        Optional<SpaceAmenity> spaceAmenity = spaceAmenityRepository.findById(id);
        if(!spaceAmenity.isPresent()) {
            return new MessageResponse("Not found space amenity with ID=" + id, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        spaceAmenity.get().setInternet(spaceAmenityDTO.isInternet());
        spaceAmenity.get().setAirConditioner(spaceAmenityDTO.isAirConditioner());
        spaceAmenity.get().setCatering(spaceAmenityDTO.isCatering());
        spaceAmenity.get().setCableTV(spaceAmenityDTO.isCableTV());
        spaceAmenity.get().setHeater(spaceAmenityDTO.isHeater());
        spaceAmenity.get().setMotel(spaceAmenityDTO.isMotel());
        spaceAmenity.get().setParking(spaceAmenityDTO.isParking());
        spaceAmenity.get().setToilet(spaceAmenityDTO.isToilet());
        spaceAmenity.get().setTv(spaceAmenityDTO.isTv());
        spaceAmenityRepository.save(spaceAmenity.get());

        return new MessageResponse("Update space amenity successfully!", HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public MessageResponse updateSpaceOperationTimes(Long spaceId, List<SpaceOperationTimeDTO> spaceOperationTimeDTOs) {
        for(SpaceOperationTimeDTO spaceOperationTimeDTO : spaceOperationTimeDTOs) {
            Optional<SpaceOperationTime> spaceOperationTime = spaceOperationTimeRepository.findById(spaceOperationTimeDTO.getId());
            if(!spaceOperationTime.isPresent()) {
                return new MessageResponse("Not found space operation time with ID=" + spaceOperationTimeDTO.getId(), HttpStatus.NOT_FOUND, LocalDateTime.now());
            }
            spaceOperationTime.get().setDay(spaceOperationTimeDTO.getDay());
            spaceOperationTime.get().setOpenTime(spaceOperationTimeDTO.getOpenTime());
            spaceOperationTime.get().setCloseTime(spaceOperationTimeDTO.getCloseTime());

            spaceOperationTimeRepository.save(spaceOperationTime.get());
        }
        return new MessageResponse("Update space operation times successfully!", HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public void deleteSpace(Long theId) {

    }

    @Override
    public Page<Space> findByNameContaining(String spaceName, Pageable pagingSort) {
        Page<Space> spacePage =  spaceRepository.findByNameContainingIgnoreCase(spaceName, pagingSort);

        return updateDataSpaces(spacePage);
    }

    private Page<Space> updateDataSpaces(Page<Space> spacePage) {
        for(Space space : spacePage.getContent()) {
            setDataSpace(space);
        }
        return  spacePage;
    }

    private void setDataSpace(Space space) {
        space.setCategoryIds(space.getCategory().getId());
        space.setSpaceDescriptionIds(space.getSpaceDescription().getId());
        space.setSpaceContactIds(space.getSpaceContact().getId());
        space.setSpaceAddressIds(space.getSpaceAddress().getId());
        space.setSpaceAmenityIds(space.getSpaceAmenity().getId());
        List<Long> spaceOperationTimeIds = new ArrayList<>();
        for(SpaceOperationTime spaceOperationTime : space.getSpaceOperationTimes()) {
            spaceOperationTimeIds.add(spaceOperationTime.getId());
        }
        space.setSpaceOperationTimeIds(spaceOperationTimeIds);
        space.setUserIds(space.getUser().getId());
    }

    @Override
    public Long count() {
        return null;
    }

    @Override
    public Long countSpacesByCategoryId(Long theCategoryId) {
        return null;
    }


    @Override
    public Page<SpaceOverviewDTO> findAllOverviewByCustomerIdPageAndSort(Long customerId, Pageable pagingSort) {
        Page<SpaceOverviewDTO> spacePage =  spaceRepository.findByUserId(customerId, pagingSort).map(this::spaceToSpaceOverviewDTO);
        return  spacePage;
    }

    @Override
    public Page<SpaceOverviewDTO> findNearByForCustomer(Long userId, Long spaceId, Pageable pagingSort) {
        Page<SpaceOverviewDTO> spacePage;

        Optional<User> user = userRepository.findByIdCustomer(userId);
        if(!user.isPresent()) {
            if (!userId.equals(Long.valueOf(0))) {
                spacePage = this.findBySearchContentOverviewContaining(null, null, null, null, null, true, false, true, false, pagingSort);
                return spacePage;
            }
        }
        Optional<Space> space = spaceRepository.findById(spaceId);
        if(!space.isPresent()) {
            spacePage = this.findBySearchContentOverviewContaining(null, null, null, null, null, true, false, true, false, pagingSort);
            return spacePage;
        }
        Set<Long> spaceIds = new HashSet<>();
        spaceIds = bookingRepository.findAllSpaceIdByUserId(userId);

        if(spaceIds.size() <= 8) {
            SpaceAddress spaceAddress = space.get().getSpaceAddress();
            String subDistrict = spaceAddress.getSubDistrict() == null ? "" : spaceAddress.getSubDistrict();
            String district = spaceAddress.getDistrict();
            String province = spaceAddress.getProvince();
            String country = spaceAddress.getCountry();
            Set<Long> nearBySpaceAddressIds = spaceAddressRepository.getAllNearBySpaceAddressIds(subDistrict, district, province, country);
            if(!CollectionUtils.isEmpty(nearBySpaceAddressIds)) {
                Set<Long> nearBySpaceIds = spaceRepository.findAllBySpaceAddressIds(nearBySpaceAddressIds);
                spaceIds.addAll(nearBySpaceIds);
            }

        }
        spaceIds.remove(spaceId);
        if(spaceIds.size() == 0) {
            spacePage = this.findBySearchContentOverviewContaining(null, null, null, null, null, true, false, true, false, pagingSort);
        } else {
            spacePage =  spaceRepository.findSpaceByIds(spaceIds, pagingSort).map(this::spaceToSpaceOverviewDTO);
        }

        return spacePage;
    }

    @Override
    public Page<SpaceOverviewDTO> findAllOverviewPageAndSort(Pageable pagingSort) {
        Page<SpaceOverviewDTO> spacePage =  spaceRepository.findAll(pagingSort).map(this::spaceToSpaceOverviewDTO);
        return  spacePage;
    }

    @Override
    public Page<SpaceOverviewDTO> findBySearchContentOverviewContaining(String spaceName, Long categoryId, String country, String province, String district, Boolean approved, Boolean notApproved, Boolean status, Boolean expired, Pageable pagingSort) {
        spaceName = (spaceName == null ? "" : spaceName);
        country = (country == null ? "" : country);
        province = (province == null ? "" : province);
        district = (district == null ? "" : district);
        Page<SpaceOverviewDTO> spacePage = null;
        //1 approved = true, notApproved = false, status = true => approved and not delete
        //2 approved = true, notApproved = false, status = false => approved but delete
        //3 approved = false, notApproved = false, status = true => waiting approve
        //4 approved = false, notApproved = true, status = true => not approved , not delete
        //5 approved = false, notApproved = true, status = false => not approved , delete
        //approved:1-2 not approved 4-5 waiting approved 3
        if(categoryId == null) {
            if(approved == null && notApproved == null && status == null && expired == null) {
                spacePage =  spaceRepository.findByNameContainingIgnoreCaseAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCase(spaceName, country, province, district, pagingSort).map(this::spaceToSpaceOverviewDTO);

            } else {
                if(expired == null) {
                    if(approved != null && notApproved != null && status == null) {
                        spacePage =  spaceRepository.findByNameContainingIgnoreCaseAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndApprovedAndNotApproved(spaceName, country, province, district, approved, notApproved, pagingSort).map(this::spaceToSpaceOverviewDTO);

                    } else if(approved == null && notApproved == null && status != null) {
                        spacePage =  spaceRepository.findByNameContainingIgnoreCaseAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndStatus(spaceName, country, province, district, status, pagingSort).map(this::spaceToSpaceOverviewDTO);

                    } else {
                        spacePage =  spaceRepository.findByNameContainingIgnoreCaseAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndApprovedAndNotApprovedAndStatus(spaceName, country, province, district, approved, notApproved, status, pagingSort).map(this::spaceToSpaceOverviewDTO);

                    }
                } else {
                    if(approved != null && notApproved != null && status == null) {
                        spacePage =  spaceRepository.findByNameContainingIgnoreCaseAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndApprovedAndNotApprovedAndExpired(spaceName, country, province, district, approved, notApproved, expired, pagingSort).map(this::spaceToSpaceOverviewDTO);

                    } else if(approved == null && notApproved == null && status != null) {
                        spacePage =  spaceRepository.findByNameContainingIgnoreCaseAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndStatusAndExpired(spaceName, country, province, district, status, expired, pagingSort).map(this::spaceToSpaceOverviewDTO);

                    } else if(approved != null && notApproved != null && status != null) {
                        spacePage =  spaceRepository.findByNameContainingIgnoreCaseAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndApprovedAndNotApprovedAndStatusAndExpired(spaceName, country, province, district, approved, notApproved, status, expired, pagingSort).map(this::spaceToSpaceOverviewDTO);

                    } else {
                        spacePage =  spaceRepository.findByNameContainingIgnoreCaseAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndExpired(spaceName, country, province, district, expired, pagingSort).map(this::spaceToSpaceOverviewDTO);
                    }
                }

            }
        } else {
            if(approved == null && notApproved == null && status == null && expired == null) {
                spacePage =  spaceRepository.findByNameContainingIgnoreCaseAndCategoryIdAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCase(spaceName, categoryId, country, province, district, pagingSort).map(this::spaceToSpaceOverviewDTO);

            } else {
                if(expired == null) {
                    if(approved != null && notApproved != null && status == null) {
                        spacePage =  spaceRepository.findByNameContainingIgnoreCaseAndCategoryIdAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndApprovedAndNotApproved(spaceName, categoryId, country, province, district, approved, notApproved, pagingSort).map(this::spaceToSpaceOverviewDTO);

                    } else if(approved == null && notApproved == null && status != null) {
                        spacePage =  spaceRepository.findByNameContainingIgnoreCaseAndCategoryIdAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndStatus(spaceName, categoryId, country, province, district, status, pagingSort).map(this::spaceToSpaceOverviewDTO);

                    } else {
                        spacePage =  spaceRepository.findByNameContainingIgnoreCaseAndCategoryIdAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndApprovedAndNotApprovedAndStatus(spaceName, categoryId, country, province, district, approved, notApproved, status, pagingSort).map(this::spaceToSpaceOverviewDTO);

                    }
                } else {
                    if(approved != null && notApproved != null && status == null) {
                        spacePage =  spaceRepository.findByNameContainingIgnoreCaseAndCategoryIdAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndApprovedAndNotApprovedAndExpired(spaceName, categoryId, country, province, district, approved, notApproved, expired, pagingSort).map(this::spaceToSpaceOverviewDTO);

                    } else if(approved == null && notApproved == null && status != null) {
                        spacePage =  spaceRepository.findByNameContainingIgnoreCaseAndCategoryIdAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndStatusAndExpired(spaceName, categoryId, country, province, district, status, expired, pagingSort).map(this::spaceToSpaceOverviewDTO);

                    } else if(approved != null && notApproved != null && status != null) {
                        spacePage =  spaceRepository.findByNameContainingIgnoreCaseAndCategoryIdAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndApprovedAndNotApprovedAndStatusAndExpired(spaceName, categoryId, country, province, district, approved, notApproved, status, expired, pagingSort).map(this::spaceToSpaceOverviewDTO);

                    } else {
                        spacePage =  spaceRepository.findByNameContainingIgnoreCaseAndCategoryIdAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndExpired(spaceName, categoryId, country, province, district, expired, pagingSort).map(this::spaceToSpaceOverviewDTO);

                    }
                }

            }
        }

       return  spacePage;
    }

    @Override
    public MessageResponse approveSpace(Long spaceId, Long userId, boolean isApproved) {
        Optional<User> user = userRepository.findByIdEmployee(userId);
        if(!user.isPresent()) {
            return new MessageResponse("Not found employee with ID=" + userId, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        Optional<Space> space = spaceRepository.findById(spaceId);
        if(!space.isPresent()) {
            return new MessageResponse("Not found space with ID=" + spaceId, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        if(space.get().isApproved() == true) {
            return new MessageResponse("Space has been processed before.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if(isApproved) {
            space.get().setApproved(true);
        } else {
            space.get().setNotApproved(true);
        }
        space.get().setModifiedBy(user.get().getName());
        space.get().setModifiedDate(new Date());
        spaceRepository.save(space.get());
        return new MessageResponse("Space has been approved successfully.", HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public MessageResponse hideSpace(Long spaceId, Long userId, boolean isHidden) {
        Optional<User> user = userRepository.findByIdEmployee(userId);
        if(!user.isPresent()) {
            return new MessageResponse("Not found employee with ID=" + userId, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        Optional<Space> space = spaceRepository.findById(spaceId);
        if(!space.isPresent()) {
            return new MessageResponse("Not found space with ID=" + spaceId, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        space.get().setStatus(isHidden ? false : true);
        space.get().setModifiedBy(user.get().getName());
        space.get().setModifiedDate(new Date());
        spaceRepository.save(space.get());
        return new MessageResponse(MessageFormat.format("Space has been {0} successfully.", isHidden ? "hidden" : "unhidden"), HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public List<String> getAllCountries() {
        return spaceAddressRepository.getAllCountries();
    }

    @Override
    public List<String> getAllProvinces() {
        return spaceAddressRepository.getAllProvinces();
    }

    @Override
    public List<String> getAllDistricts() {
        return spaceAddressRepository.getAllDistricts();
    }

    @Override
    public MessageResponse paymentSpace(Long spaceId, Long packageId) {
        Optional<Space> space = spaceRepository.findById(spaceId);
        if(!space.isPresent()) {
            return new MessageResponse("Not found space with ID=" + spaceId, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        if(space.get().isApproved() == false) {
            return new MessageResponse("Space need to approve before payment.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        Optional<ServicePack> servicePack = servicePackRepository.findById(packageId);
        if(!servicePack.isPresent()) {
            return new MessageResponse("Not found service package with ID=" + packageId, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        Long totalPayments = spacePaymentRepository.countBySpaceId(spaceId);
        SpacePayment spacePayment = new SpacePayment();
        spacePayment.setSpace(space.get());
        spacePayment.setServicePackName(servicePack.get().getName());
        spacePayment.setPrice(servicePack.get().getPrice());
        spacePayment.setCreatedDate(new Date());
        spacePayment.setOutOfDate(false);
        if(Long.valueOf(0).equals(totalPayments)) {
            spacePayment.setExpiredTime(CommonUtils.addMonths(new Date(), servicePack.get().getPeriod()));
        } else {
            SpacePayment oldSpacePayment = spacePaymentRepository.findBySpaceIdAndOutOfDate(spaceId);
            oldSpacePayment.setOutOfDate(true);
            spacePaymentRepository.save(oldSpacePayment);

            Date maxExpiredTime = oldSpacePayment.getExpiredTime();
            Date newExpiredTime = maxExpiredTime.compareTo(new Date()) > 0 ? maxExpiredTime : new Date();
            spacePayment.setExpiredTime(CommonUtils.addMonths(newExpiredTime, servicePack.get().getPeriod()));
        }


        space.get().setPaid(true);
        space.get().setExpired(false);
        spaceRepository.save(space.get());
        spacePaymentRepository.save(spacePayment);
        return new MessageResponse("Space has been paid successfully.", HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public MessageResponse processExpiredSpace() {
        List<Long> spaceIds = spacePaymentRepository.findAllGroupBySpaceId();
        List<String> expiredSpaceIds = new ArrayList<>();
        for (Long spaceId : spaceIds) {
            Date maxExpiredTime = spacePaymentRepository.getMaxExpiredTimeBySpaceId(spaceId);
            if (maxExpiredTime.compareTo(new Date()) <= 0) {
                Space space = spaceRepository.getById(spaceId);
                space.setExpired(true);
                space.setPaid(false);
                spaceRepository.save(space);
                expiredSpaceIds.add(spaceId.toString());
            }
        }
        return new MessageResponse( expiredSpaceIds.size() == 0 ? "No expired space" : String.format("Spaces with id = %s need renew.", String.join(", ", expiredSpaceIds)), HttpStatus.OK, LocalDateTime.now());
    }


    public SpaceOverviewDTO spaceToSpaceOverviewDTO(Space space) {
        SpaceOverviewDTO spaceDTO = new SpaceOverviewDTO();
        spaceDTO.setId(space.getId());
        spaceDTO.setName(space.getName());
        spaceDTO.setPrice(space.getPrice());
        spaceDTO.setUnit(space.getUnit());
        spaceDTO.setLargeImage(space.getLargeImage());
        spaceDTO.setAddress(space.getSpaceAddress().getAddressLine1());
        spaceDTO.setCountry(space.getSpaceAddress().getCountry());
        spaceDTO.setProvince(space.getSpaceAddress().getProvince());
        spaceDTO.setDistrict(space.getSpaceAddress().getDistrict());
        spaceDTO.setRatingAverage(space.getRatingAverage());
        spaceDTO.setCategoryId(space.getCategory().getId());
        spaceDTO.setStatus(space.isStatus());
        spaceDTO.setApproved(space.isApproved());
        spaceDTO.setNotApproved(space.isNotApproved());
        spaceDTO.setNumberOfRoom(space.getNumberOfRoom());
        spaceDTO.setPaid(space.isPaid());
        spaceDTO.setExpiredDate(spacePaymentRepository.getMaxExpiredTimeBySpaceId(space.getId()));
        spaceDTO.setExpired(space.isExpired());
        spaceDTO.setUserId(space.getUser().getId());

        return spaceDTO;
    }

    @Override
    public Page<SpaceOverviewDTO> findBySearchContentOverviewContainingForCustomer(String content, Pageable pagingSort) {
        content = (content == null ? "" : content);
        Page<SpaceOverviewDTO> spacePage = null;

        spacePage =  spaceRepository.findByNameContainingIgnoreCaseOrSpaceAddress_CountryContainingIgnoreCaseOrSpaceAddress_ProvinceContainingIgnoreCaseOrSpaceAddress_DistrictContainingIgnoreCase(content, content, content, content, pagingSort)
                .map(this::spaceToSpaceOverviewDTO);

        List<SpaceOverviewDTO> spaceOverviewDTOS = spacePage.getContent().stream().filter(SpaceOverviewDTO::isApproved).filter(x -> !x.isNotApproved()).filter(SpaceOverviewDTO::isStatus).filter(x -> !x.isExpired()).collect(Collectors.toList());

        return  new PageImpl<>(spaceOverviewDTOS, pagingSort, spaceOverviewDTOS.size());
    }


    @Override
    public List<SubSpace> findMatchSpace(MatchSubSpaceDTO matchSubSpaceDTO) {
        if(matchSubSpaceDTO.getStartDate().compareTo(matchSubSpaceDTO.getEndDate()) >= 0) {
            return null;
        }
        List<SubSpace> matchPeopleSubSpaces = subSpaceRepository.findBySpaceIdAndNumberOfPeopleGreaterThanEqual(matchSubSpaceDTO.getSpaceId(), matchSubSpaceDTO.getNumberOfPeople());
        if(CollectionUtils.isEmpty(matchPeopleSubSpaces)) {
            return null;
        }
        List<SubSpace> subSpaces = new ArrayList<>();
        Date s2 = matchSubSpaceDTO.getStartDate();
        Date e2 = matchSubSpaceDTO.getEndDate();
        for(SubSpace subSpace : matchPeopleSubSpaces) {
            List<Booking> bookings = bookingRepository.findBySubSpaceIdAndStatusNotDone(subSpace.getId(), EnumUtils.getEnum(BookingStatus.class, "PENDING"),
                    EnumUtils.getEnum(BookingStatus.class, "BOOKED"));
            if(CollectionUtils.isEmpty(bookings)) {
                subSpaces.add(subSpace);
                continue;
            }
            List<Boolean> checkNotOverLap = new ArrayList<>();
            for(Booking booking : bookings) {
                Date s1 = booking.getStartDate();
                Date e1 = booking.getEndDate();
                if(s1.before(s2) && e1.after(s2) ||
                        s1.before(e2) && e1.after(e2) ||
                        s1.before(s2) && e1.after(e2) ||
                        s1.after(s2) && e1.before(e2) ) {
                    checkNotOverLap.add(false);
                } else {
                    checkNotOverLap.add(true);
                }
            }

            if(checkNotOverLap.stream().allMatch(x -> x == true)) {
                subSpaces.add(subSpace);
            }
        }
        return subSpaces;
    }
}


