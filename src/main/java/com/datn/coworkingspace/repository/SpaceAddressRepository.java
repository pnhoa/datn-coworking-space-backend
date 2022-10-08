package com.datn.coworkingspace.repository;

import com.datn.coworkingspace.entity.SpaceAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpaceAddressRepository extends JpaRepository<SpaceAddress, Long> {

    @Query(value = "SELECT s.country FROM SpaceAddress s GROUP BY s.country")
    List<String> getAllCountries();

    @Query(value = "SELECT s.province FROM SpaceAddress s GROUP BY s.province")
    List<String> getAllProvinces();

    @Query(value = "SELECT s.district FROM SpaceAddress s GROUP BY s.district")
    List<String> getAllDistricts();

}
