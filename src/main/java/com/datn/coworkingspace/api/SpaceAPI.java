package com.datn.coworkingspace.api;

import com.datn.coworkingspace.dto.*;
import com.datn.coworkingspace.entity.Space;
import com.datn.coworkingspace.entity.SubSpace;
import com.datn.coworkingspace.service.ISpaceService;
import com.datn.coworkingspace.utils.CommonUtils;
import com.datn.coworkingspace.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/spaces")
@CrossOrigin
public class SpaceAPI {

    @Autowired
    private ISpaceService spaceService;

    @GetMapping("/detail")
    public ResponseEntity<?> findAllDetails( @RequestParam(name = "q", required = false) String spaceName,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int limit,
                                      @RequestParam(defaultValue = "id,ASC") String[] sort){

        try {

            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);
            Page<Space> spacePage = null;

            if(spaceName == null) {
                spacePage = spaceService.findAllPageAndSort(pagingSort);
            } else {
                spacePage = spaceService.findByNameContaining(spaceName, pagingSort);
            }


            return new ResponseEntity<>(spacePage, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/overview")
    public ResponseEntity<?> findAllOverview( @RequestParam(name = "q", required = false) String spaceName,
                                      @RequestParam(name = "categoryId", required = false) Long categoryId,
                                      @RequestParam(name = "country", required = false) String country,
                                      @RequestParam(name = "province", required = false) String province,
                                      @RequestParam(name = "district", required = false) String district,
                                      @RequestParam(name = "approved", required = false) Boolean approved,
                                      @RequestParam(name = "notApproved", required = false) Boolean notApproved,
                                      @RequestParam(name = "status", required = false) Boolean status,
                                      @RequestParam(name = "expired", required = false) Boolean expired,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int limit,
                                      @RequestParam(defaultValue = "id,ASC") String[] sort){

        try {

            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);
            Page<SpaceOverviewDTO> spacePage = null;

            if(StringUtils.isBlank(spaceName) && categoryId == null && StringUtils.isBlank(province) && StringUtils.isBlank(country)
                    && StringUtils.isBlank(district) && approved == null && status == null && expired == null) {
                spacePage = spaceService.findAllOverviewPageAndSort(pagingSort);
            } else  {
                spacePage = spaceService.findBySearchContentOverviewContaining(spaceName, categoryId, country, province, district, approved, notApproved, status, expired, pagingSort);
            }


            return new ResponseEntity<>(spacePage, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<?> findAllOverviewByCustomerId(
                                              @PathVariable("customerId") Long customerId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int limit,
                                              @RequestParam(defaultValue = "id,ASC") String[] sort){

        try {

            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);
            Page<SpaceOverviewDTO> spacePage =  spaceService.findAllOverviewByCustomerIdPageAndSort(customerId, pagingSort);


            return new ResponseEntity<>(spacePage, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Space> findById(@PathVariable("id") Long theId){
        Space theSpace = spaceService.findById(theId);
        return new ResponseEntity<>(theSpace, HttpStatus.OK);
    }

    @PostMapping(value = "",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createSpace(@Valid @RequestPart(value = "theSpaceDto") SpaceDTO theSpaceDto,
                                         @RequestPart(value = "largeFile") MultipartFile largeFile,
                                         @RequestPart(value = "files", required = false) MultipartFile[] files,
                                         @RequestPart(value = "subSpaceFiles", required = false) MultipartFile[] subSpaceFiles, BindingResult theBindingResult) {

        if(theBindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for create space", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }
        if(largeFile == null || !FileUtils.checkImageFile(largeFile.getOriginalFilename()) || (files != null && files.length > 5)) {
            return new ResponseEntity<>(new MessageResponse("Invalid value for create space", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = spaceService.createSpace(theSpaceDto, largeFile, files, subSpaceFiles);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSpace(@PathVariable("id") Long theId,
                                                         @Valid @RequestBody SpaceDTO theSpaceDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for update space", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = spaceService.updateSpace(theId, theSpaceDto);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping("/description/{id}")
    public ResponseEntity<?> updateSpaceDescription(@PathVariable("id") Long theId,
                                                    @Valid @RequestBody SpaceDescriptionDTO theSpaceDescriptionDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for update space description", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = spaceService.updateSpaceDescription(theId, theSpaceDescriptionDto);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping("/address/{id}")
    public ResponseEntity<?> updateSpaceAddress(@PathVariable("id") Long theId,
                                                @Valid @RequestBody SpaceAddressDTO theSpaceAddressDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for update space address", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = spaceService.updateSpaceAddress(theId, theSpaceAddressDto);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping("/contact/{id}")
    public ResponseEntity<?> updateSpaceContact(@PathVariable("id") Long theId,
                                                @Valid @RequestBody SpaceContactDTO theSpaceContactDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for update space contact", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = spaceService.updateSpaceContact(theId, theSpaceContactDto);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping("/amenity/{id}")
    public ResponseEntity<?> updateSpaceAmenity(@PathVariable("id") Long theId,
                                                @Valid @RequestBody SpaceAmenityDTO theSpaceAmenityDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for update space amenity", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = spaceService.updateSpaceAmenity(theId, theSpaceAmenityDto);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping("/operationTimes/{id}")
    public ResponseEntity<?> updateSpaceOperationTimes(@PathVariable("id") Long theId,
                                                       @Valid @RequestBody List<SpaceOperationTimeDTO> theSpaceAmenityDtos, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for update space amenity", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = spaceService.updateSpaceOperationTimes(theId, theSpaceAmenityDtos);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping("/approve/{id}/{userId}/{bool}")
    public ResponseEntity<?> approveSpace(@PathVariable("id") Long spaceId, @PathVariable("userId") Long userId, @PathVariable("bool") boolean bool) {
        MessageResponse messageResponse = spaceService.approveSpace(spaceId, userId, bool);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping("/hide/{id}/{userId}/{bool}")
    public ResponseEntity<?> hideSpace(@PathVariable("id") Long spaceId, @PathVariable("userId") Long userId, @PathVariable("bool") boolean bool) {
        MessageResponse messageResponse = spaceService.hideSpace(spaceId, userId, bool);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @GetMapping("/countries")
    public ResponseEntity<?> findAllCountries(){

        List<String> countries = spaceService.getAllCountries();
        return new ResponseEntity<>(countries, HttpStatus.OK);
    }

    @GetMapping("/provinces")
    public ResponseEntity<?> findAllProvinces(){

        List<String> provinces = spaceService.getAllProvinces();
        return new ResponseEntity<>(provinces, HttpStatus.OK);
    }

    @GetMapping("/districts")
    public ResponseEntity<?> findAllDistricts(){

        List<String> districts = spaceService.getAllDistricts();
        return new ResponseEntity<>(districts, HttpStatus.OK);
    }

    @PutMapping("/payment/{id}/{packageId}")
    public ResponseEntity<?> paymentSpace(@PathVariable("id") Long spaceId, @PathVariable("packageId") Long packageId) {
        MessageResponse messageResponse = spaceService.paymentSpace(spaceId, packageId);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @GetMapping("")
    public ResponseEntity<?> findAllOverviewAndSearchForCustomer( @RequestParam(name = "q", required = false) String content,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int limit,
                                              @RequestParam(defaultValue = "id,ASC") String[] sort){

        try {

            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);
            Page<SpaceOverviewDTO> spacePage = null;

            if(StringUtils.isBlank(content)) {
                spacePage = spaceService.findBySearchContentOverviewContaining(null, null, null, null, null, true, false, true, false,pagingSort);
            } else  {
                spacePage = spaceService.findBySearchContentOverviewContainingForCustomer(content, pagingSort);
            }

            return new ResponseEntity<>(spacePage, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/processExpiredSpace")
    public ResponseEntity<?> processExpiredSpace(){

        MessageResponse messageResponse = spaceService.processExpiredSpace();
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PostMapping("/find")
    public ResponseEntity<?> findMatchSubSpace(@Valid @RequestBody MatchSubSpaceDTO matchSubSpaceDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for find match sup space", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }
        List<SubSpace> subSpaces = spaceService.findMatchSpace(matchSubSpaceDTO);

        if(CollectionUtils.isEmpty(subSpaces)) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }

        return new ResponseEntity<>(subSpaces, HttpStatus.OK);
    }
}
