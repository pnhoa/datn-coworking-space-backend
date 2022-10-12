package com.datn.coworkingspace.repository;

import com.datn.coworkingspace.entity.SpacePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface SpacePaymentRepository extends JpaRepository<SpacePayment, Long> {

    @Query("SELECT MAX(s.expiredTime) from SpacePayment s WHERE s.space.id=?1")
    Date getMaxExpiredTimeBySpaceId(Long spaceId);

    @Query("SELECT s from SpacePayment s WHERE s.space.id=?1")
    List<SpacePayment> findBySpaceId(Long spaceId);

}
