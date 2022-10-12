package com.datn.coworkingspace.service;

import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.dto.ServicePackDTO;
import com.datn.coworkingspace.entity.ServicePack;
import com.datn.coworkingspace.exception.ResourceNotFoundException;
import com.datn.coworkingspace.repository.ServicePackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ServicePackService implements IServicePackService {

    @Autowired
    private ServicePackRepository servicePackRepository;

    @Override
    public List<ServicePack> findAll() {
        List<ServicePack> servicePacks = servicePackRepository.findAll();

        return servicePacks;
    }

    @Override
    public ServicePack findById(Long theId) throws ResourceNotFoundException {
        Optional<ServicePack> servicePack = servicePackRepository.findById(theId);
        if(!servicePack.isPresent()) {
            throw  new ResourceNotFoundException("Not found service pack with ID=" + theId);
        } else {
            return servicePack.get();
        }
    }

    @Override
    public Optional<ServicePack> findByIdOptional(Long theId) {
        return servicePackRepository.findById(theId);
    }

    @Override
    public MessageResponse createServicePack(ServicePackDTO theServicePackDto) {

        ServicePack theServicePack = new ServicePack();

        theServicePack.setCreatedDate(new Date());
        theServicePack.setCreatedBy(theServicePackDto.getCreatedBy());
        theServicePack.setName(theServicePackDto.getName());
        theServicePack.setPeriod(theServicePackDto.getPeriod());
        theServicePack.setPrice(theServicePackDto.getPrice());

        servicePackRepository.save(theServicePack);

        return new MessageResponse("Create service pack successfully!", HttpStatus.CREATED, LocalDateTime.now());
    }

    @Override
    public MessageResponse updateServicePack(Long theId, ServicePackDTO theServicePackDto) {
        Optional<ServicePack> theServicePack = servicePackRepository.findById(theId);

        if(!theServicePack.isPresent()) {
            throw new ResourceNotFoundException("Not found service pack with ID=" + theId);
        } else {

            if(!theServicePack.get().getName().equals(theServicePackDto.getName())){
                if(servicePackRepository.existsByName(theServicePackDto.getName())){
                    return new MessageResponse("Error: service pack name is already use.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
                }
            }

            theServicePack.get().setModifiedDate(new Date());
            theServicePack.get().setModifiedBy(theServicePackDto.getModifiedBy());
            theServicePack.get().setName(theServicePackDto.getName());
            theServicePack.get().setPrice(theServicePackDto.getPrice());
            theServicePack.get().setPeriod(theServicePackDto.getPeriod());

            servicePackRepository.save(theServicePack.get());
        }

        return new MessageResponse("Updated service pack successfully!", HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public MessageResponse deleteServicePack(Long theId) {
        ServicePack theServicePack = servicePackRepository.findById(theId).orElseThrow(
                () -> new ResourceNotFoundException("Not found service pack with ID=" + theId));

        servicePackRepository.delete(theServicePack);
        return new MessageResponse("Deleted successfully!", HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public Page<ServicePack> findAllPageAndSort(Pageable pagingSort) {
        Page<ServicePack> servicePackPage =  servicePackRepository.findAll(pagingSort);

        return  servicePackPage;
    }

    @Override
    public Page<ServicePack> findByNameContaining(String servicePackName, Pageable pagingSort) {
        Page<ServicePack> servicePackPage =  servicePackRepository.findByNameContainingIgnoreCase(servicePackName, pagingSort);

        return  servicePackPage;
    }

    @Override
    public Boolean existsByName(String name) {
        return servicePackRepository.existsByName(name);
    }
}
