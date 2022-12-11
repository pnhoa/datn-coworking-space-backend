package com.datn.coworkingspace.service;

import com.datn.coworkingspace.dto.BookingDTO;
import com.datn.coworkingspace.dto.BookingStatusDTO;
import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IBookingService {

    Page<Booking> findAllPageAndSort(Pageable pagingSort);

    Booking findById(Long theId);

    MessageResponse createBooking(BookingDTO theBookingDto);

    MessageResponse updateBooking(Long theId, BookingDTO theBookingDto);

    Page<Booking> findBySearchContentContaining(String content, String status, Pageable pagingSort);

    MessageResponse updateStatusBooking(long theId, BookingStatusDTO statusDto);

    Page<Booking> findByCustomerIdPageAndSort(Long customerId, Pageable pagingSort);

    Page<Booking> findAllByUserIdPageAndSort(Long userId, Pageable pagingSort);

    Page<Booking> findByUserIdAndSearchContentContaining(Long userId, String content, String status, Pageable pagingSort);
}
