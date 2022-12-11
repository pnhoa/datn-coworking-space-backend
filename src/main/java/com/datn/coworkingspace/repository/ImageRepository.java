package com.datn.coworkingspace.repository;

import com.datn.coworkingspace.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

   Optional<Image> findById(Long id);

   List<Image> findBySpaceId(Long postId);

}
