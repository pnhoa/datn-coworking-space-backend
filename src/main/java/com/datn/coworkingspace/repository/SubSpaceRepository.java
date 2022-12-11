package com.datn.coworkingspace.repository;

import com.datn.coworkingspace.entity.SubSpace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubSpaceRepository extends JpaRepository<SubSpace, Long> {

    List<SubSpace> findBySpaceIdAndNumberOfPeopleGreaterThanEqual(Long spaceId, Integer numberOfPeople);
}
