package com.datn.coworkingspace.repository;

import com.datn.coworkingspace.entity.ServicePack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicePackRepository extends JpaRepository<ServicePack, Long> {

    Page<ServicePack> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Boolean existsByName(String name);

}
