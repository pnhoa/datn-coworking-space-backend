package com.datn.coworkingspace.service;

import com.datn.coworkingspace.dto.*;
import com.datn.coworkingspace.entity.PasswordResetToken;
import com.datn.coworkingspace.entity.Role;
import com.datn.coworkingspace.entity.User;
import com.datn.coworkingspace.enums.AuthProvider;
import com.datn.coworkingspace.exception.ResourceNotFoundException;
import com.datn.coworkingspace.mapper.UserMapper;
import com.datn.coworkingspace.repository.PasswordResetTokenRepository;
import com.datn.coworkingspace.repository.RoleRepository;
import com.datn.coworkingspace.repository.UserRepository;
import com.datn.coworkingspace.utils.FileUtils;
import com.datn.coworkingspace.utils.UserDetailsImpl;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    @Qualifier("passwordEncoder")
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${bezkoder.app.jwtResetExpirationMs}")
    private Long resetTokenDurationMs;

    @Autowired
    private StorageService storageService;

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<EmployeeDTO> findAllEmployee() {
        List<User> theEmployees = userRepository.findAllEmployee();

        List<EmployeeDTO> theEmployeesDto = new ArrayList<>();

        for(User theEmployee :theEmployees) {
            if(theEmployee.getEnabled() == 1) {
                theEmployeesDto.add(UserMapper.mapperToDTO(theEmployee));
            }
        }

        return theEmployeesDto;
    }

    @Override
    public EmployeeDTO findByIdEmployeeDto(Long theId) {
        Optional<User> theEmployee = userRepository.findByIdEmployee(theId);

        if(!theEmployee.isPresent()) {
            throw new ResourceNotFoundException("Not found user with ID=" + theId);
        } else {
            if(theEmployee.get().getEnabled() == 1) {
                return UserMapper.mapperToDTO(theEmployee.get());
            }
        }

        return null;
    }



    @Override
    public MessageResponse createEmployee(EmployeeDTO theEmployeeDto, MultipartFile file) {

        Boolean existEmployee = userRepository.existsByUserName(theEmployeeDto.getUserName());

        if(existEmployee == true){
            return new MessageResponse("Exist username in database", HttpStatus.CONFLICT, LocalDateTime.now());
        } else {
            User theEmployee = new User();

            theEmployee.setCreatedDate(new Date());
            theEmployee.setUserName(theEmployeeDto.getUserName());
            theEmployee.setName(theEmployeeDto.getName());
            theEmployee.setPassword(passwordEncoder.encode(theEmployeeDto.getPassword()));
            theEmployee.setEmail(theEmployeeDto.getEmail());
            theEmployee.setPhoneNumber(theEmployeeDto.getPhoneNumber());
            theEmployee.setAddress(theEmployeeDto.getAddress());
            theEmployee.setGender(theEmployeeDto.getGender());
            if(file != null && FileUtils.checkImageFile(file.getOriginalFilename()) ) {
                String profilePicture = storageService.uploadFile(file, FileUtils.generateProfileUUID());
                theEmployee.setProfilePicture(profilePicture.replace(" ", ""));
            }
            theEmployee.setEnabled(1);
            theEmployee.setAccCustomer(false);
            theEmployee.setProvider(AuthProvider.LOCAL);

            if(theEmployeeDto.getRoleCode() == null) {
                return new MessageResponse("Error create Employee!", HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
            }

            Set<Role> roles;
            if(theEmployeeDto.getRoleCode().equals("ROLE_ADMIN")) {
                roles = new HashSet<>(Arrays.asList(roleRepository.findByCode("ROLE_EMPLOYEE"),
                        roleRepository.findByCode("ROLE_ADMIN")));
            } else {
                roles = new HashSet<>(Arrays.asList(roleRepository.findByCode("ROLE_EMPLOYEE")));
            }
            theEmployee.setRoles(roles);


            userRepository.save(theEmployee);

            return new MessageResponse("Create employee successfully!", HttpStatus.CREATED, LocalDateTime.now());
        }
    }

    @Override
    public MessageResponse updateEmployee(Long theId, EmployeeDTO theEmployeeDto, MultipartFile file) {
        Optional<User> theEmployee = userRepository.findById(theId);

        if(!theEmployee.isPresent()){
            throw new ResourceNotFoundException("Not found employee with ID=" + theId);
        } else {
            theEmployee.get().setModifiedDate(new Date());
            theEmployee.get().setModifiedBy(theEmployee.get().getUserName());
            theEmployee.get().setName(theEmployeeDto.getName());
            theEmployee.get().setEmail(theEmployeeDto.getEmail());
            theEmployee.get().setPhoneNumber(theEmployeeDto.getPhoneNumber());
            theEmployee.get().setAddress(theEmployeeDto.getAddress());
            theEmployee.get().setGender(theEmployeeDto.getGender());
            if(file != null && FileUtils.checkImageFile(file.getOriginalFilename())) {
                String profilePicture = storageService.uploadFile(file, FileUtils.generateProfileUUID());
                theEmployee.get().setProfilePicture(profilePicture.replace(" ", ""));
            }
            theEmployee.get().setAccCustomer(false);

            Set<Role> roles;
            if(theEmployeeDto.getRoleCode().equals("ROLE_ADMIN")) {
                roles = new HashSet<>(Arrays.asList(roleRepository.findByCode("ROLE_EMPLOYEE"),
                        roleRepository.findByCode("ROLE_ADMIN")));
            } else {
                roles = new HashSet<>(Arrays.asList(roleRepository.findByCode("ROLE_EMPLOYEE")));
            }
            theEmployee.get().setRoles(roles);

            userRepository.save(theEmployee.get());
        }

        return new MessageResponse("Updated employee successfully!", HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public void deleteEmployee(Long theId) {
        Optional<User> theEmployee = userRepository.findById(theId);

        if(!theEmployee.isPresent()) {
            throw new ResourceNotFoundException("Not found Employee with ID=" + theId);
        } else {
            theEmployee.get().setEnabled(0);
            userRepository.save(theEmployee.get());
        }
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByUserName(String username) {
        return userRepository.existsByUserName(username);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        Optional<User> user = userRepository.findByEmail(userName);
        return user;
    }


    @Override
    public Page<User> findAllPageAndSortEmployee(Pageable pagingSort) {
        Page<User> employeePage =  userRepository.findByIsAccCustomer(false, pagingSort);

        return getUsers(employeePage);
    }

    @Override
    public Page<User> findByUserNameContainingEmployee(String userName, Pageable pagingSort) {
        Page<User> employeePage =  userRepository.findByUserNameContainingAndIsAccCustomer(userName,false, pagingSort);

        return getUsers(employeePage);
    }

    private Page<User> getUsers(Page<User> employeePage) {
        for(User employee : employeePage.getContent()) {
                employee.setProfilePicture(employee.getProfilePicture());
            if(employee.getRoles().size() == 2) {
                employee.setRoleCode("ROLE_ADMIN");
            } else {
                employee.setRoleCode("ROLE_EMPLOYEE");
            }
        }
        return  employeePage;
    }

    @Override
    public Long countEmployee() {
        return userRepository.countEmployee();
    }

    @Override
    public List<CustomerDTO> findAllCustomer() {
        List<User> theCustomers = userRepository.findAllCustomer();

        List<CustomerDTO> theCustomersDto = new ArrayList<>();

        for(User theCustomer :theCustomers) {
            if(theCustomer.getEnabled() == 1) {
                theCustomersDto.add(UserMapper.mapperToCustomerDTO(theCustomer));
            }
        }

        return theCustomersDto;
    }

    @Override
    public CustomerDTO findByIdCustomerDto(Long theId) {
        Optional<User> theCustomer = userRepository.findByIdCustomer(theId);

        if(!theCustomer.isPresent()) {
            throw new ResourceNotFoundException("Not found user with ID=" + theId);
        } else {
            if(theCustomer.get().getEnabled() == 1) {
                return UserMapper.mapperToCustomerDTO(theCustomer.get());
            }
        }

        return null;
    }

    @Override
    public MessageResponse createCustomer(CustomerDTO theCustomerDto) {
        Boolean existCustomer = userRepository.existsByUserName(theCustomerDto.getUserName());

        if(existCustomer == true){
            return new MessageResponse("Exist username in database", HttpStatus.CONFLICT, LocalDateTime.now());
        } else {
            User theCustomer = new User();

            theCustomer.setCreatedDate(new Date());
            theCustomer.setUserName(theCustomerDto.getUserName());
            theCustomer.setName(theCustomerDto.getName());
            theCustomer.setPassword(passwordEncoder.encode(theCustomerDto.getPassword()));
            theCustomer.setEmail(theCustomerDto.getEmail());
            theCustomer.setPhoneNumber(theCustomerDto.getPhoneNumber());
            theCustomer.setAddress(theCustomerDto.getAddress());
            theCustomer.setGender(theCustomerDto.getGender());
            if(theCustomerDto.getProfilePicture() != null ) {
                theCustomer.setProfilePicture(theCustomerDto.getProfilePicture());
            }
            theCustomer.setEnabled(1);
            theCustomer.setAccCustomer(true);
            theCustomer.setProvider(AuthProvider.LOCAL);
            Set<Role> roles = new HashSet<>(Arrays.asList(roleRepository.findByCode("ROLE_CUSTOMER")));
            theCustomer.setRoles(roles);

            userRepository.save(theCustomer);

            return new MessageResponse("Create customer successfully!", HttpStatus.CREATED, LocalDateTime.now());
        }

    }

    @Override
    public MessageResponse updateCustomer(Long theId, CustomerDTO theCustomerDto, MultipartFile file) {
        Optional<User> theCustomer = userRepository.findByIdCustomer(theId);

        if(!theCustomer.isPresent()){
            throw new ResourceNotFoundException("Not found customer with ID=" + theId);
        } else {
            theCustomer.get().setModifiedDate(new Date());
            theCustomer.get().setModifiedBy(theCustomer.get().getUserName());
            theCustomer.get().setName(theCustomerDto.getName());
            theCustomer.get().setEmail(theCustomerDto.getEmail());
            theCustomer.get().setPhoneNumber(theCustomerDto.getPhoneNumber());
            theCustomer.get().setAddress(theCustomerDto.getAddress());
            theCustomer.get().setGender(theCustomerDto.getGender());
            String oldProfilePicture = null;
            if(file != null && FileUtils.checkImageFile(file.getOriginalFilename())) {
                String profilePicture = storageService.uploadFile(file, FileUtils.generateProfileUUID());
                oldProfilePicture = theCustomer.get().getProfilePicture();
                theCustomer.get().setProfilePicture(profilePicture.replace(" ", ""));
            }
            theCustomer.get().setAccCustomer(true);

            userRepository.save(theCustomer.get());
            if(StringUtils.isNoneBlank(oldProfilePicture)) {
                String fileName = FilenameUtils.getName(oldProfilePicture);
                //storageService.deleteFile(fileName);
            }

        }

        return new MessageResponse("Updated customer successfully!", HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public void deleteCustomer(Long theId) {
        Optional<User> theCustomer = userRepository.findByIdCustomer(theId);

        if(!theCustomer.isPresent()){
            throw new ResourceNotFoundException("Not found customer with ID=" + theId);
        } else {
            theCustomer.get().setEnabled(0);
            userRepository.save(theCustomer.get());
        }
    }

    @Override
    public Page<User> findAllPageAndSortCustomer(Pageable pagingSort) {
        Page<User> customerPage =  userRepository.findByIsAccCustomer(true,pagingSort);
        return  customerPage;
    }

    @Override
    public Page<User> findByUserNameContainingCustomer(String userName, Pageable pagingSort) {
        Page<User> customerPage =  userRepository.findByUserNameContainingAndIsAccCustomer(userName, true, pagingSort);
        return  customerPage;
    }

    @Override
    public Long countCustomer() {
        return userRepository.countCustomer();
    }

    @Override
    public User findByIdCustomer(Long customerId) {
        Optional<User> theCustomer = userRepository.findByIdCustomer(customerId);

        if(!theCustomer.isPresent()) {
            throw new ResourceNotFoundException("Not found user with ID=" + customerId);
        } else {
            if(theCustomer.get().getEnabled() == 1) {
                return theCustomer.get();
            }
        }

        return null;
    }

    @Override
    public MessageResponse resetPassword(PasswordResetRequest request, String siteURL) throws MessagingException, UnsupportedEncodingException{
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if(user.isPresent()){
            String randomCode = RandomString.make(64);

            sendResetPasswordEmail(user.get(), siteURL, randomCode);

            PasswordResetToken passwordResetToken = new PasswordResetToken();
            passwordResetToken.setToken(randomCode);
            passwordResetToken.setUser(user.get());
            passwordResetToken.setExpiryDate(Instant.now().plusMillis(resetTokenDurationMs));

            passwordResetTokenRepository.save(passwordResetToken);

            return new MessageResponse("Please check mail to reset password.", HttpStatus.OK, LocalDateTime.now());
        } else {
            return new MessageResponse("Email not in database", HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
    }

    @Override
    public MessageResponse changeResetPassword(PasswordResetChangeRequest request) {

        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if(user.isPresent()){

            PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUserId(user.get().getId());

            if(passwordResetToken != null) {
                passwordResetTokenRepository.delete(passwordResetToken);

                String encodedPassword = passwordEncoder.encode(request.getPassword());
                user.get().setPassword(encodedPassword);
                user.get().setModifiedDate(new Date());

                userRepository.save(user.get());
                return new MessageResponse("Reset password successfully!", HttpStatus.OK, LocalDateTime.now());
            } else {
                return new MessageResponse("Password has been reset.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
            }


        } else {
            return new MessageResponse("Email not in database", HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
    }

    @Override
    public MessageResponse changePassword(PasswordChangeRequest request) {

        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if(user.isPresent()){

            if(passwordEncoder.matches(request.getOldPassword(), user.get().getPassword())){
                String encodedPassword = passwordEncoder.encode(request.getPassword());
                user.get().setPassword(encodedPassword);
                user.get().setModifiedDate(new Date());

                userRepository.save(user.get());

                return new MessageResponse("Update password successfully!", HttpStatus.OK, LocalDateTime.now());
            } else {
                return new MessageResponse("The old password is incorrect!", HttpStatus.BAD_REQUEST, LocalDateTime.now());
            }

        } else {
            return new MessageResponse("Email not in database", HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
    }

    @Override
    public Page<User> findByEnabledEmployee(Integer enabled, Pageable pagingSort) {
        Page<User> employeePage =  userRepository.findByEnabledAndIsAccCustomer(enabled, false, pagingSort);

        return  employeePage;
    }

    @Override
    public Page<User> findByUserNameContainingAndEnabledEmployee(String userName, Integer enabled, Pageable pagingSort) {
        Page<User> employeePage =  userRepository.findByUserNameContainingAndEnabledAndIsAccCustomer(userName ,enabled, false, pagingSort);

        return  employeePage;
    }

    @Override
    public Page<User> findByEnabledCustomer(Integer enabled, Pageable pagingSort) {
        Page<User> customerPage =  userRepository.findByEnabledAndIsAccCustomer(enabled, true, pagingSort);

        return  customerPage;
    }

    @Override
    public Page<User> findByUserNameContainingAndEnabledCustomer(String userName, Integer enabled, Pageable pagingSort) {
        Page<User> customerPage =  userRepository.findByUserNameContainingAndEnabledAndIsAccCustomer(userName, enabled, true, pagingSort);

        return  customerPage;
    }

    @Override
    public MessageResponse activeCustomer(Long userId) {
        Optional<User> customer = userRepository.findByIdCustomer(userId);
        if(!customer.isPresent()) {
            return new MessageResponse("Not find customer with ID= " + userId, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        if(customer.get().getEnabled() == 1){
            return new MessageResponse("Customer has been active", HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        customer.get().setEnabled(1);
        userRepository.save(customer.get());
        return new MessageResponse("Enable customer successfully!", HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public MessageResponse activeEmployee(Long userId) {
        Optional<User> employee = userRepository.findByIdEmployee(userId);
        if(!employee.isPresent()) {
            return new MessageResponse("Not find employee with ID= " + userId, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        if(employee.get().getEnabled() == 1){
            return new MessageResponse("Employee has been active", HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        employee.get().setEnabled(1);
        userRepository.save(employee.get());
        return new MessageResponse("Enable employee successfully!", HttpStatus.OK, LocalDateTime.now());
    }

    private void sendResetPasswordEmail(User user, String siteURL, String randomCode) throws MessagingException, UnsupportedEncodingException{
        String toAddress = user.getEmail();
        String fromAddress = "cnpmt12022@gmail.com";
        String senderName = "ABC Store";
        String subject = "Please confirm to reset password";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to reset your password:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">RESET</a></h3>"
                + "Thank you,<br>"
                + "ABC Store.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getName());
        String verifyURL = siteURL + "/api/password/confirm?token=" + randomCode;

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }

    @Override
    public User findByIdEmployee(Long employeeId) {
        Optional<User> theEmployee = userRepository.findById(employeeId);

        if(!theEmployee.isPresent()) {
            throw new ResourceNotFoundException("Not found user with ID=" + employeeId);
        } else {
            if(theEmployee.get().getEnabled() == 1) {
                return theEmployee.get();
            }
        }

        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(username);

        if ( (!user.isPresent()) || user.get().getEnabled() == 0) {
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }

        return UserDetailsImpl.build(user.get());
    }
}
