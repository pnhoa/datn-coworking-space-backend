package com.datn.coworkingspace.repository;

import com.datn.coworkingspace.entity.SpacePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface SpacePaymentRepository extends JpaRepository<SpacePayment, Long> {

    @Query("SELECT MAX(s.expiredTime) from SpacePayment s WHERE s.space.id=?1 AND s.outOfDate = false")
    Date getMaxExpiredTimeBySpaceId(Long spaceId);

    @Query("SELECT s from SpacePayment s WHERE s.space.id=?1")
    List<SpacePayment> findBySpaceId(Long spaceId);

    @Query("SELECT s from SpacePayment s WHERE s.space.id=?1 AND s.outOfDate = false ")
    SpacePayment findBySpaceIdAndOutOfDate(Long spaceId);

    @Query("SELECT COUNT(s) from SpacePayment s WHERE s.space.id=?1")
    Long countBySpaceId(Long spaceId);

    @Query("SELECT s.space.id from SpacePayment s WHERE s.outOfDate = false GROUP BY s.space.id")
    List<Long> findAllGroupBySpaceId();

    @Query("SELECT SUM(s.price) from SpacePayment s WHERE s.createdDate>=?1 AND s.createdDate<?2")
    BigDecimal getAllRevenueByDay(Timestamp day, Timestamp dayEnd);

}
