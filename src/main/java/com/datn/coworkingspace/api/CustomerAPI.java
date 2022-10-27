package com.datn.coworkingspace.api;

import com.datn.coworkingspace.dto.CustomerDTO;
import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.entity.User;
import com.datn.coworkingspace.service.IUserService;
import com.datn.coworkingspace.utils.CommonUtils;
import com.datn.coworkingspace.validationgroups.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin
public class CustomerAPI {

    @Autowired
    private IUserService customerService;

    @GetMapping("")
    public ResponseEntity<Page<User>> findAll(@RequestParam(name = "q", required = false) String userName,
                                              @RequestParam(name = "enabled", required = false) Integer enabled,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int limit,
                                              @RequestParam(defaultValue = "id,ASC") String[] sort){

        try {

            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);
            Page<User> customerPage;

            if(userName == null) {
                customerPage = customerService.findAllPageAndSortCustomer(pagingSort);
            } else {
                customerPage = customerService.findByUserNameContainingCustomer(userName, pagingSort);
            }
            if(userName == null && enabled == null) {
                customerPage = customerService.findAllPageAndSortCustomer(pagingSort);
            } else {
                if(enabled == null){
                    customerPage = customerService.findByUserNameContainingCustomer(userName, pagingSort);
                } else if (userName == null) {
                    customerPage = customerService.findByEnabledCustomer(enabled, pagingSort);
                } else {
                    customerPage = customerService.findByUserNameContainingAndEnabledCustomer(userName, enabled, pagingSort);
                }

            }

            return new ResponseEntity<>(customerPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> findById(@PathVariable("id") Long theId){

        CustomerDTO theCustomer = customerService.findByIdCustomerDto(theId);
        return new ResponseEntity<>(theCustomer, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> updateCustomer(@PathVariable("id") Long theId,
                                                          @Validated(OnUpdate.class) @RequestPart(value = "theCustomerDto") CustomerDTO theCustomerDto,
                                                          @RequestPart(value = "file", required = false) MultipartFile file, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<MessageResponse>(new MessageResponse("Invalid value for update customer", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = customerService.updateCustomer(theId, theCustomerDto, file);
        return new ResponseEntity<MessageResponse>(messageResponse, messageResponse.getStatus());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("id") Long theId){

        customerService.deleteCustomer(theId);
        return new ResponseEntity<>(new MessageResponse("Delete successfully!", HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }


    @GetMapping("/count")
    public ResponseEntity<?> count(){
        return new ResponseEntity<>(customerService.countCustomer(), HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<MessageResponse> activeCustomer(@RequestParam(name = "username", required = true) String userName){

        MessageResponse messageResponse = customerService.activeCustomer(userName);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }
}
