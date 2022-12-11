package com.datn.coworkingspace.api;

import com.datn.coworkingspace.dto.EmployeeDTO;
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
@RequestMapping("/api/employees")
@CrossOrigin
public class EmployeeAPI {

    @Autowired
    private IUserService employeeService;

    @GetMapping("")
    public ResponseEntity<Page<User>> findAll(@RequestParam(name = "q", required = false) String userName,
                                              @RequestParam(name = "enabled", required = false) Integer enabled,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int limit,
                                              @RequestParam(defaultValue = "id,ASC") String[] sort){

        try {

            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);
            Page<User> employeePage;

            if(userName == null && enabled == null) {
                employeePage = employeeService.findAllPageAndSortEmployee(pagingSort);
            } else {
                if(enabled == null){
                    employeePage = employeeService.findByUserNameContainingEmployee(userName, pagingSort);
                } else if (userName == null) {
                    employeePage = employeeService.findByEnabledEmployee(enabled, pagingSort);
                } else {
                    employeePage = employeeService.findByUserNameContainingAndEnabledEmployee(userName, enabled, pagingSort);
                }

            }

            return new ResponseEntity<>(employeePage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> findById(@PathVariable("id") Long theId){

        EmployeeDTO theEmployee = employeeService.findByIdEmployeeDto(theId);
        return new ResponseEntity<>(theEmployee, HttpStatus.OK);
    }

    @PostMapping(value = "",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> createEmployee(@Valid @RequestPart(value = "theEmployeeDto") EmployeeDTO theEmployeeDto,
                                                          @RequestPart(value = "file", required = false) MultipartFile file, BindingResult theBindingResult){

        if(theBindingResult.hasErrors()){
            return new ResponseEntity<MessageResponse>(new MessageResponse("Invalid value for create employee", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        if(employeeService.existsByUserName(theEmployeeDto.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already use.", HttpStatus.BAD_REQUEST, LocalDateTime.now()));

        }

        if(employeeService.existsByEmail(theEmployeeDto.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already use.", HttpStatus.BAD_REQUEST, LocalDateTime.now()));

        }

        MessageResponse messageResponse = employeeService.createEmployee(theEmployeeDto, file);
        return new ResponseEntity<MessageResponse>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> updateEmployee(@PathVariable("id") Long theId,
                                                          @Validated(OnUpdate.class)  @RequestPart(value = "theEmployeeDto")  EmployeeDTO theEmployeeDto,
                                                          @RequestPart(value = "file", required = false) MultipartFile file, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<MessageResponse>(new MessageResponse("Invalid value for update employee", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = employeeService.updateEmployee(theId, theEmployeeDto, file);
        return new ResponseEntity<MessageResponse>(messageResponse, messageResponse.getStatus());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Long theId){

        employeeService.deleteEmployee(theId);
        return new ResponseEntity<>(new MessageResponse("Delete successfully!", HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }


    @GetMapping("/count")
    public ResponseEntity<?> count(){
        return new ResponseEntity<>(employeeService.countEmployee(), HttpStatus.OK);
    }

    @PutMapping("/active/{userId}")
    public ResponseEntity<MessageResponse> activeEmployee(@PathVariable("userId") Long userId){

        MessageResponse messageResponse = employeeService.activeEmployee(userId);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }
}
