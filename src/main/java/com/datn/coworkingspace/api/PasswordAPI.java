package com.datn.coworkingspace.api;

import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.dto.PasswordChangeRequest;
import com.datn.coworkingspace.dto.PasswordResetChangeRequest;
import com.datn.coworkingspace.dto.PasswordResetRequest;
import com.datn.coworkingspace.entity.PasswordResetToken;
import com.datn.coworkingspace.service.IPasswordResetTokenService;
import com.datn.coworkingspace.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/password")
@CrossOrigin
public class PasswordAPI {

    @Autowired
    private IUserService userService;

    @Autowired
    private IPasswordResetTokenService passwordResetTokenService;

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetRequest request, BindingResult theBindingResult, HttpServletRequest requestHttp) throws MessagingException, UnsupportedEncodingException {

        if(theBindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for reset password", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = userService.resetPassword(request, getSiteURL(requestHttp));

        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> verifyResetPasswordToken(@RequestParam("token") String token) {
        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenService.findByToken(token);
        if(passwordResetToken.isPresent()) {
            if(passwordResetTokenService.verifyExpiration(passwordResetToken.get()) == true) {
                Map<String, Object> map = new HashMap<>();
                map.put("email", passwordResetToken.get().getUser().getEmail());
                return new ResponseEntity<>(map ,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(new MessageResponse("Sorry your token is expired.", HttpStatus.BAD_REQUEST, LocalDateTime.now()),HttpStatus.BAD_REQUEST);
            }

        } else {
            return new ResponseEntity<>(new MessageResponse("Token is incorrect or it maybe already confirm.", HttpStatus.BAD_REQUEST, LocalDateTime.now()),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/change")
    public ResponseEntity<?> changeResetPassword(@Valid @RequestBody PasswordResetChangeRequest request, BindingResult theBindingResult) {
        if(theBindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for change password", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse =  userService.changeResetPassword(request);

        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PostMapping("/edit")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeRequest request, BindingResult theBindingResult) {
        if(theBindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for edit password", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse =  userService.changePassword(request);

        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }


    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }



}
