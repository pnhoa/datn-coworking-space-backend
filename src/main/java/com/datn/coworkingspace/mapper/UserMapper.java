package com.datn.coworkingspace.mapper;

import com.datn.coworkingspace.dto.CustomerDTO;
import com.datn.coworkingspace.dto.EmployeeDTO;
import com.datn.coworkingspace.entity.User;
import org.springframework.util.Base64Utils;

public class UserMapper {
    public static EmployeeDTO mapperToDTO(User theEmployee) {

        EmployeeDTO theEmployeeDto = new EmployeeDTO();

        theEmployeeDto.setId(theEmployee.getId());
        theEmployeeDto.setUserName(theEmployee.getUserName());
        theEmployeeDto.setName(theEmployee.getName());
        theEmployeeDto.setEmail(theEmployee.getEmail());
        theEmployeeDto.setPhoneNumber(theEmployee.getPhoneNumber());
        theEmployeeDto.setAddress(theEmployee.getAddress());
        theEmployeeDto.setGender(theEmployee.getGender());
        theEmployeeDto.setProfilePicture(theEmployee.getProfilePicture());
        theEmployeeDto.setEnabled(theEmployee.getEnabled());

        if(theEmployee.getRoles().size() == 2) {
            theEmployeeDto.setRoleCode("ROLE_ADMIN");
        } else {
            theEmployeeDto.setRoleCode("ROLE_EMPLOYEE");
        }

        return theEmployeeDto;
    }

    public static CustomerDTO mapperToCustomerDTO(User theCustomer){
        CustomerDTO theCustomerDto = new CustomerDTO();

        theCustomerDto.setId(theCustomer.getId());
        theCustomerDto.setUserName(theCustomer.getUserName());
        theCustomerDto.setName(theCustomer.getName());
        theCustomerDto.setEmail(theCustomer.getEmail());
        theCustomerDto.setPhoneNumber(theCustomer.getPhoneNumber());
        theCustomerDto.setAddress(theCustomer.getAddress());
        theCustomerDto.setGender(theCustomer.getGender());
        theCustomerDto.setProfilePicture(theCustomer.getProfilePicture());
        theCustomerDto.setEnabled(theCustomer.getEnabled());
        theCustomerDto.setRoleCode("ROLE_CUSTOMER");

        return theCustomerDto;
    }

}
