package com.datn.coworkingspace.repository;

import com.datn.coworkingspace.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCompanyNameContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(String name, String email, String companyName, String phoneNumber, Pageable pageable);

}
