package com.datn.coworkingspace.repository;

import com.datn.coworkingspace.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findBySpaceId( Long spaceId, Pageable pagingSort);

    List<Comment> findBySpaceId(Long spaceId);

    Page<Comment> findByRate(int rating, Pageable pageable);

    Optional<Comment> findByUserIdAndSpaceId(Long customerId, Long spaceId);
}
