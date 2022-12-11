package com.datn.coworkingspace.api;

import com.datn.coworkingspace.dto.BookingDTO;
import com.datn.coworkingspace.dto.BookingStatusDTO;
import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.entity.Booking;
import com.datn.coworkingspace.service.IBookingService;
import com.datn.coworkingspace.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin
public class BookingAPI {

    @Autowired
    private IBookingService bookingService;

    @GetMapping(value = {"","/"})
    public ResponseEntity<?> getAll(@RequestParam(name = "content", required = false) String content,
                                    @RequestParam(name = "status", required = false) String status,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "20") int limit,
                                    @RequestParam(defaultValue = "id,DESC") String[] sort){

        try {

            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);
            Page<Booking> bookingPage = null;

            if(StringUtils.isBlank(content) && StringUtils.isBlank(status)) {
                bookingPage = bookingService.findAllPageAndSort(pagingSort);
            } else  {
                bookingPage = bookingService.findBySearchContentContaining(content, status, pagingSort);
            }

            return new ResponseEntity<>(bookingPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/management/{userId}"})
    public ResponseEntity<?> getAllByOwner(@PathVariable("userId") Long userId,
                                    @RequestParam(name = "content", required = false) String content,
                                    @RequestParam(name = "status", required = false) String status,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "20") int limit,
                                    @RequestParam(defaultValue = "id,DESC") String[] sort){

        try {

            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);
            Page<Booking> bookingPage = null;

            if(StringUtils.isBlank(content) && StringUtils.isBlank(status)) {
                bookingPage = bookingService.findAllByUserIdPageAndSort(userId, pagingSort);
            } else  {
                bookingPage = bookingService.findByUserIdAndSearchContentContaining(userId, content, status, pagingSort);
            }

            return new ResponseEntity<>(bookingPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long theId){
        Booking booking = bookingService.findById(theId);

        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<?> findAllBookingsByCustomer(@PathVariable("customerId") Long customerId,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "20") int limit,
                                                    @RequestParam(defaultValue = "id,ASC") String[] sort){
        try {
            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);

            Page<Booking> bookingPage = bookingService.findByCustomerIdPageAndSort(customerId, pagingSort);

            return new ResponseEntity<>(bookingPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse("NOT_FOUND", HttpStatus.NOT_FOUND, LocalDateTime.now()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("")
    public ResponseEntity<MessageResponse> createBooking(@Valid @RequestBody BookingDTO theBookingDto, BindingResult theBindingResult){

        if(theBindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for create booking", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = bookingService.createBooking(theBookingDto);
        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateBooking(@PathVariable("id") Long theId,
                                                         @Valid @RequestBody BookingDTO theBookingDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for update booking", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = bookingService.updateBooking(theId, theBookingDto);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<MessageResponse> updateStatusCart(@PathVariable("id") long theId,
                                                            @Validated @RequestBody BookingStatusDTO statusDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for update status booking", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = bookingService.updateStatusBooking(theId, statusDto);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

}
