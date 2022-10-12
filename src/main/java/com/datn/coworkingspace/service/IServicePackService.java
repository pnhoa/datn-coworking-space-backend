package com.datn.coworkingspace.service;

import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.dto.ServicePackDTO;
import com.datn.coworkingspace.entity.ServicePack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IServicePackService {

    List<ServicePack> findAll();

    ServicePack findById(Long theId);

    Optional<ServicePack> findByIdOptional(Long theId);

    MessageResponse createServicePack(ServicePackDTO theServicePackDto);

    MessageResponse updateServicePack(Long theId, ServicePackDTO theServicePackDto);

    MessageResponse deleteServicePack(Long theId);

    Page<ServicePack> findAllPageAndSort(Pageable pagingSort);

    Page<ServicePack> findByNameContaining(String categoryName, Pageable pagingSort);

    Boolean existsByName(String name);
}
