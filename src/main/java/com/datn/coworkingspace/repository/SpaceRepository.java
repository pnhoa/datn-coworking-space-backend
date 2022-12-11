package com.datn.coworkingspace.repository;

import com.datn.coworkingspace.entity.Space;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface SpaceRepository extends JpaRepository<Space, Long> {

    @Query("SELECT s from Space s WHERE s.category.id=?1")
    List<Space> findSpacesByCategoryId(Long categoryId);

    @Query("SELECT s FROM Space s WHERE lower(s.name) LIKE %?1%" )
    List<Space> search( String key);

    Page<Space> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Space> findByNameContainingIgnoreCaseAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndApprovedAndNotApprovedAndStatus(
            String name, String country, String province, String district, Boolean approved, Boolean notApproved, Boolean status, Pageable pageable );

    Page<Space> findByNameContainingIgnoreCaseAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCase(
            String name, String country, String province, String district, Pageable pageable );

    Page<Space> findByNameContainingIgnoreCaseAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndApprovedAndNotApproved(
            String name, String country, String province, String district, Boolean approved, Boolean notApproved, Pageable pageable );

    Page<Space> findByNameContainingIgnoreCaseAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndStatus(
            String name, String country, String province, String district, Boolean status, Pageable pageable );

    Page<Space> findByNameContainingIgnoreCaseAndCategoryIdAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndApprovedAndNotApprovedAndStatus(
            String name, Long categoryId, String country, String province, String district, Boolean approved, Boolean notApproved, Boolean status, Pageable pageable );

    Page<Space> findByNameContainingIgnoreCaseAndCategoryIdAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCase(
            String name, Long categoryId, String country, String province, String district, Pageable pageable );

    Page<Space> findByNameContainingIgnoreCaseAndCategoryIdAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndApprovedAndNotApproved(
            String name, Long categoryId, String country, String province, String district, Boolean approved, Boolean notApproved, Pageable pageable );

    Page<Space> findByNameContainingIgnoreCaseAndCategoryIdAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndStatus(
            String name, Long categoryId, String country, String province, String district, Boolean status, Pageable pageable );


    Page<Space> findByNameContainingIgnoreCaseAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndApprovedAndNotApprovedAndExpired(
            String name, String country, String province, String district, Boolean approved, Boolean notApproved, Boolean expired, Pageable pageable );

    Page<Space> findByNameContainingIgnoreCaseAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndStatusAndExpired(
            String name, String country, String province, String district, Boolean status, Boolean expired, Pageable pageable );

    Page<Space> findByNameContainingIgnoreCaseAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndApprovedAndNotApprovedAndStatusAndExpired(
            String name, String country, String province, String district, Boolean approved, Boolean notApproved, Boolean status, Boolean expired, Pageable pageable );

    Page<Space> findByNameContainingIgnoreCaseAndCategoryIdAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndApprovedAndNotApprovedAndStatusAndExpired(
            String name, Long categoryId, String country, String province, String district, Boolean approved, Boolean notApproved, Boolean status, Boolean expired, Pageable pageable );

    Page<Space> findByNameContainingIgnoreCaseAndCategoryIdAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndApprovedAndNotApprovedAndExpired(
            String spaceName, Long categoryId, String country, String province, String district, Boolean approved, Boolean notApproved, Boolean expired, Pageable pagingSort);

    Page<Space> findByNameContainingIgnoreCaseAndCategoryIdAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndStatusAndExpired(
            String spaceName, Long categoryId, String country, String province, String district, Boolean status, Boolean expired, Pageable pagingSort);


    Page<Space> findByNameContainingIgnoreCaseAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndExpired(
            String spaceName, String country, String province, String district, Boolean expired, Pageable pagingSort);

    Page<Space> findByNameContainingIgnoreCaseAndCategoryIdAndSpaceAddress_CountryContainingIgnoreCaseAndSpaceAddress_ProvinceContainingIgnoreCaseAndSpaceAddress_DistrictContainingIgnoreCaseAndExpired(
            String spaceName, Long categoryId, String country, String province, String district, Boolean expired, Pageable pagingSort);

    @Query("SELECT count(s) from Space s WHERE s.category.id=?1")
    Long countSpacesByCategoryId(Long categoryId);


    @Query(value = "SELECT s FROM Space s WHERE s.id IN :listId AND s.approved=true AND s.notApproved=false AND s.status=true AND s.expired=false ")
    Page<Space> findSpaceByIds(Set<Long> listId, Pageable pagingSort);

    Page<Space> findByNameContainingIgnoreCaseOrSpaceAddress_CountryContainingIgnoreCaseOrSpaceAddress_ProvinceContainingIgnoreCaseOrSpaceAddress_DistrictContainingIgnoreCase(String spaceName, String country, String province, String district, Pageable pagingSort);

    Page<Space> findByUserId(Long customerId, Pageable pagingSort);

    @Query(value = "SELECT s.id FROM Space s WHERE s.spaceAddress.id IN :nearBySpaceAddressIds")
    Set<Long> findAllBySpaceAddressIds(Set<Long> nearBySpaceAddressIds);

    @Query(value = "SELECT count(s) FROM Space s WHERE s.approved=true AND s.notApproved=false AND s.status=true AND s.expired=false ")
    Long countSpaceActive();
}
