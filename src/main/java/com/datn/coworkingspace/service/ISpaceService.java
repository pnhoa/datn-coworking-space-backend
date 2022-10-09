package com.datn.coworkingspace.service;

import com.datn.coworkingspace.dto.*;
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

    Page<SpaceOverviewDTO> findBySearchContentOverviewContaining(String spaceName, Long categoryId, String country, String province, String district, Boolean approved, Boolean notApproved, Boolean status, Pageable pagingSort);

    MessageResponse approveSpace(Long spaceId, Long userId, boolean isApproved);

    MessageResponse hideSpace(Long spaceId, Long userId, boolean isHidden);

    List<String> getAllCountries();

    List<String> getAllProvinces();

    List<String> getAllDistricts();
}
