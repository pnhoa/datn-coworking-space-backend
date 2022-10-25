package com.datn.coworkingspace.repository;

import com.datn.coworkingspace.entity.Booking;
import com.datn.coworkingspace.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCompanyNameContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(String name, String email, String companyName, String phoneNumber, Pageable pageable);

    @Query("SELECT b from Booking b WHERE b.subSpace.id=?1 AND (b.status = ?2 OR b.status = ?3) ")
    List<Booking> findBySubSpaceIdAndStatusNotDone(Long subSpaceId, BookingStatus pending, BookingStatus booked);
}
