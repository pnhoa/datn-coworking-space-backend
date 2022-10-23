package com.datn.coworkingspace.api;

import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.dto.ServicePackDTO;
import com.datn.coworkingspace.entity.ServicePack;
import com.datn.coworkingspace.service.IServicePackService;
import com.datn.coworkingspace.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/servicePacks")
@CrossOrigin
public class ServicePackAPI {

    @Autowired
    private IServicePackService servicePackService;

    @GetMapping(value = {"","/"})
    public ResponseEntity<List<ServicePack>> getAll(@RequestParam(name = "q", required = false) String servicePackName,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "20") int limit,
                                                    @RequestParam(defaultValue = "id,ASC") String[] sort){

        try {

            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);
            Page<ServicePack> servicePackPage;

            if(servicePackName == null) {
                servicePackPage = servicePackService.findAllPageAndSort(pagingSort);
            } else {
                servicePackPage = servicePackService.findByNameContaining(servicePackName, pagingSort);
            }


            return new ResponseEntity<>(servicePackPage.getContent(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicePack> findById(@PathVariable("id") Long theId){
        ServicePack theServicePack = servicePackService.findById(theId);

        return new ResponseEntity<>(theServicePack, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<MessageResponse> createServicePack(@Valid @RequestBody ServicePackDTO theServicePackDto, BindingResult theBindingResult){

        if(theBindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for create service pack", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        if(servicePackService.existsByName(theServicePackDto.getName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Service pack name is already use.", HttpStatus.BAD_REQUEST, LocalDateTime.now()));
        }

        MessageResponse messageResponse = servicePackService.createServicePack(theServicePackDto);
        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateServicePack(@PathVariable("id") Long theId,
                                                         @Valid @RequestBody ServicePackDTO theServicePackDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for update category", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = servicePackService.updateServicePack(theId, theServicePackDto);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteServicePack(@PathVariable("id") Long theId){

        MessageResponse messageResponse = servicePackService.deleteServicePack(theId);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

}
