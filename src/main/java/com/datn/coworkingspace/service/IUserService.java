package com.datn.coworkingspace.service;

import com.datn.coworkingspace.dto.*;
import com.datn.coworkingspace.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

public interface IUserService extends UserDetailsService {

    List<EmployeeDTO> findAllEmployee();

    EmployeeDTO findByIdEmployeeDto(Long theId);


    MessageResponse createEmployee(EmployeeDTO theEmployee, MultipartFile file);

    MessageResponse updateEmployee(Long theId, EmployeeDTO theEmployee, MultipartFile file);

    void deleteEmployee(Long theId);

    Boolean existsByEmail(String email);

    Boolean existsByUserName(String username);

    Optional<User> findByUserName(String userName);

    User findByIdEmployee(Long employeeId);

    Page<User> findAllPageAndSortEmployee(Pageable pagingSort);

    Page<User> findByUserNameContainingEmployee(String userName, Pageable pagingSort);

    Long countEmployee();

    List<CustomerDTO> findAllCustomer();

    CustomerDTO findByIdCustomerDto(Long theId);


    MessageResponse createCustomer(CustomerDTO theCustomer);

    MessageResponse updateCustomer(Long theId, CustomerDTO theCustomer, MultipartFile file);

    void deleteCustomer(Long theId);

    Page<User> findAllPageAndSortCustomer(Pageable pagingSort);

    Page<User> findByUserNameContainingCustomer(String userName, Pageable pagingSort);

    Long countCustomer();

    User findByIdCustomer(Long customerId);

    MessageResponse resetPassword(PasswordResetRequest request, String getSiteURL) throws MessagingException, UnsupportedEncodingException;

    MessageResponse changeResetPassword(PasswordResetChangeRequest request);

    MessageResponse changePassword(PasswordChangeRequest request);

    Page<User> findByEnabledEmployee(Integer enabled, Pageable pagingSort);

    Page<User> findByUserNameContainingAndEnabledEmployee(String userName, Integer enabled, Pageable pagingSort);

    Page<User> findByEnabledCustomer(Integer enabled, Pageable pagingSort);

    Page<User> findByUserNameContainingAndEnabledCustomer(String userName, Integer enabled, Pageable pagingSort);

    MessageResponse activeCustomer(Long userId);

    MessageResponse activeEmployee(Long userId);
}
