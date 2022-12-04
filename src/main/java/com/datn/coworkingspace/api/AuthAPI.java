package com.datn.coworkingspace.api;

import com.datn.coworkingspace.dto.*;
import com.datn.coworkingspace.entity.RefreshToken;
import com.datn.coworkingspace.entity.User;
import com.datn.coworkingspace.exception.TokenRefreshException;
import com.datn.coworkingspace.service.IBlacklistService;
import com.datn.coworkingspace.service.IRefreshTokenService;
import com.datn.coworkingspace.service.IUserService;
import com.datn.coworkingspace.service.StorageService;
import com.datn.coworkingspace.utils.FileUtils;
import com.datn.coworkingspace.utils.JwtUtils;
import com.datn.coworkingspace.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthAPI {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserService customerService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private IRefreshTokenService refreshTokenService;

    @Autowired
    private IBlacklistService blacklistService;

    @Autowired
    private StorageService storageService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateCustomer(@Valid @RequestBody LoginDTO loginDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()) {
            return new ResponseEntity<MessageResponse>(new MessageResponse("Invalid value for login", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }
        Optional<User> user = customerService.findByUserName(loginDto.getUserName());
        if(user.isPresent()) {
            if(user.get().getEnabled() == 0) {
                return new ResponseEntity<>(new MessageResponse("Account is blocked.", HttpStatus.UNAUTHORIZED, LocalDateTime.now()), HttpStatus.UNAUTHORIZED);
            }
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUserName(),
                        loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl customerDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = customerDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(customerDetails.getId());

        if(roles.contains("ROLE_EMPLOYEE")){
            return new ResponseEntity<>(new MessageResponse("Account is denied to customer page", HttpStatus.UNAUTHORIZED,LocalDateTime.now()), HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(new JwtResponse(jwt,
                refreshToken.getToken(),
                customerDetails.getId(),
                customerDetails.getUsername(),
                customerDetails.getEmail()));
    }

    @PostMapping(value = "/signup",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerCustomer(@Valid @RequestPart(value = "customerDto") CustomerDTO customerDto,
                                              @RequestPart(value = "file", required = false) MultipartFile file, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for create customer", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        if(customerService.existsByUserName(customerDto.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already use.", HttpStatus.BAD_REQUEST, LocalDateTime.now()));

        }

        if(customerService.existsByEmail(customerDto.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already use.", HttpStatus.BAD_REQUEST, LocalDateTime.now()));
        }

        if(file != null) {
            if(FileUtils.checkImageFile(file.getOriginalFilename())) {
                String profilePicture = storageService.uploadFile(file, FileUtils.generateProfileUUID());
                customerDto.setProfilePicture(profilePicture.replace(" ", ""));
            } else {
                customerDto.setProfilePicture("");
            }

        }

        MessageResponse messageResponse = customerService.createCustomer(customerDto);

        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());

    }

    @PostMapping("refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value.", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }
        String refreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateJwtTokenFromUsername(user.getUserName());

                    return ResponseEntity.ok(new JwtResponse(token,
                            refreshToken,
                            user.getId(),
                            user.getUserName(),
                            user.getEmail()));
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken,"Refresh token isn't in database!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody LogoutRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value.", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = blacklistService.addTokenToBlacklist(request);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

}
