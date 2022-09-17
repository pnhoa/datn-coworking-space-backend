package com.datn.coworkingspace.api;

import com.datn.coworkingspace.dto.*;
import com.datn.coworkingspace.entity.RefreshToken;
import com.datn.coworkingspace.exception.TokenRefreshException;
import com.datn.coworkingspace.service.IBlacklistService;
import com.datn.coworkingspace.service.IRefreshTokenService;
import com.datn.coworkingspace.service.IUserService;
import com.datn.coworkingspace.utils.JwtUtils;
import com.datn.coworkingspace.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/auth")
@CrossOrigin
public class AuthAdminAPI {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserService employeeService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private IRefreshTokenService refreshTokenService;

    @Autowired
    private IBlacklistService blacklistService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateEmployee(@Valid @RequestBody LoginDTO loginDto, BindingResult theBindingResult){

        if(theBindingResult.hasErrors()) {
            return new ResponseEntity<MessageResponse>(new MessageResponse("Invalid value for login", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUserName(),
                        loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl employeeDetails = (UserDetailsImpl) authentication.getPrincipal();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(employeeDetails.getId());

        List<String> roles = employeeDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        if(roles.contains("ROLE_CUSTOMER")){
            return new ResponseEntity<>(new MessageResponse("Account is denied to admin", HttpStatus.UNAUTHORIZED,LocalDateTime.now()), HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(new JwtResponse(jwt,
                refreshToken.getToken(),
                employeeDetails.getId(),
                employeeDetails.getUsername(),
                employeeDetails.getEmail()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerEmployee(@Valid @RequestBody EmployeeDTO employeeDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for create employee", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        if(employeeService.existsByUserName(employeeDto.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already use.", HttpStatus.BAD_REQUEST, LocalDateTime.now()));

        }

        if(employeeService.existsByEmail(employeeDto.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already use.", HttpStatus.BAD_REQUEST, LocalDateTime.now()));

        }


        MessageResponse messageResponse =  employeeService.createEmployee(employeeDto);

        return  new ResponseEntity<>(messageResponse, messageResponse.getStatus());

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
