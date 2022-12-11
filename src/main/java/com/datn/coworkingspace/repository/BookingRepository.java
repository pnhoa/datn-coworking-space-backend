package com.datn.coworkingspace.repository;

import com.datn.coworkingspace.entity.Booking;
import com.datn.coworkingspace.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCompanyNameContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(String name, String email, String companyName, String phoneNumber, Pageable pageable);

    @Query("SELECT b from Booking b WHERE b.subSpace.id=?1 AND (b.status = ?2 OR b.status = ?3) ")
    List<Booking> findBySubSpaceIdAndStatusNotDone(Long subSpaceId, BookingStatus pending, BookingStatus booked);

    Page<Booking> findByUserId(Long customerId, Pageable pagingSort);

    Page<Booking> findBySpaceOwnerId(Long spaceOwnerId, Pageable pagingSort);

    @Query("SELECT b.spaceId FROM Booking b WHERE b.user.id=?1 GROUP BY b.spaceId")
    Set<Long> findAllSpaceIdByUserId(Long userId);

    @Query("SELECT b.subSpace.packageSubSpace.serviceSpace.space.category.id AS categoryId, b.subSpace.packageSubSpace.serviceSpace.space.category.name AS categoryName, SUM(b.totalPrice) AS quantity from Booking b WHERE b.createdDate>=?1 AND b.createdDate<?2 GROUP BY b.subSpace.packageSubSpace.serviceSpace.space.category.id, b.subSpace.packageSubSpace.serviceSpace.space.category.name ORDER BY b.subSpace.packageSubSpace.serviceSpace.space.category.id ASC")
    List<Map<String, Object>> getTotalSpaceBookingGroupByCategory(Timestamp date, Timestamp dateEndDate);

    @Query("SELECT b.spaceId AS productId, b.subSpace.packageSubSpace.serviceSpace.space.name AS productName, COUNT(b.spaceId) AS quantity, SUM(b.totalPrice) AS totalPrice from Booking b WHERE b.createdDate>=?1 AND b.createdDate<?2 AND b.status <> ?3  GROUP BY b.spaceId, b.subSpace.packageSubSpace.serviceSpace.space.name, b.subSpace.packageSubSpace.serviceSpace.space.price ORDER BY SUM(b.totalPrice) DESC")
    List<Map<String, Object>> getAllTopSpaceByDay(Timestamp date, Timestamp dateEndDate, BookingStatus cancelled, PageRequest of);
}
