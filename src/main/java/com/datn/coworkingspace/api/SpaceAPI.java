package com.datn.coworkingspace.api;

import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.dto.SpaceDTO;
import com.datn.coworkingspace.entity.Space;
import com.datn.coworkingspace.service.ISpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/spaces")
@CrossOrigin
public class SpaceAPI {

    @Autowired
    private ISpaceService spaceService;

    @GetMapping("/{id}")
    public ResponseEntity<Space> findById(@PathVariable("id") Long theId){

        Space theSpace = spaceService.findById(theId);
        return new ResponseEntity<>(theSpace, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<MessageResponse> createSpace(@Valid @RequestBody SpaceDTO theSpaceDto, BindingResult theBindingResult){

        if(theBindingResult.hasErrors()){
            return new ResponseEntity<MessageResponse>(new MessageResponse("Invalid value for create space", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = spaceService.createSpace(theSpaceDto);
        return new ResponseEntity<MessageResponse>(messageResponse, messageResponse.getStatus());
    }
}
