package com.datn.coworkingspace.service;

import com.datn.coworkingspace.dto.*;
import com.datn.coworkingspace.entity.Space;
import com.datn.coworkingspace.entity.SubSpace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ISpaceService {

    List<Space> findAll();

    Page<Space> findAllPageAndSort(Pageable pagingSort);

    Space findById(Long theId);

    MessageResponse createSpace(SpaceDTO theSpaceDto, MultipartFile largeFile, MultipartFile[] files, MultipartFile[] subSpaceFiles);

    MessageResponse updateSpace(Long theId, SpaceDTO theSpaceDto);

    MessageResponse updateSpaceDescription(Long id, SpaceDescriptionDTO spaceDescriptionDTO);

    MessageResponse updateSpaceAddress(Long id, SpaceAddressDTO spaceAddressDTO);

    MessageResponse updateSpaceContact(Long id, SpaceContactDTO spaceContactDTO);

    MessageResponse updateSpaceAmenity(Long id, SpaceAmenityDTO spaceAmenityDTO);

    MessageResponse updateSpaceOperationTimes(Long spaceId, List<SpaceOperationTimeDTO> spaceOperationTimeDTOs);

    void deleteSpace(Long theId);

    Page<Space> findByNameContaining(String spaceName, Pageable pagingSort);

    Long count();

    Long countSpacesByCategoryId(Long theCategoryId);

    Page<SpaceOverviewDTO> findAllOverviewPageAndSort(Pageable pagingSort);

    Page<SpaceOverviewDTO> findBySearchContentOverviewContaining(String spaceName, Long categoryId, String country, String province, String district, Boolean approved, Boolean notApproved, Boolean status, Boolean expired, Pageable pagingSort);

    Page<SpaceOverviewDTO> findBySearchContentOverviewContainingForCustomer(String content, Pageable pagingSort);


    MessageResponse approveSpace(Long spaceId, Long userId, boolean isApproved);

    MessageResponse hideSpace(Long spaceId, Long userId, boolean isHidden);

    List<String> getAllCountries();

    List<String> getAllProvinces();

    List<String> getAllDistricts();

    MessageResponse paymentSpace(Long spaceId, Long packageId);

    MessageResponse processExpiredSpace();

    List<SubSpace> findMatchSpace(MatchSubSpaceDTO matchSubSpaceDTO);

    Page<SpaceOverviewDTO> findAllOverviewByCustomerIdPageAndSort(Long customerId, Pageable pagingSort);
}
