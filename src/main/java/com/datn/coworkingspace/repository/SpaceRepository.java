package com.datn.coworkingspace.repository;

import com.datn.coworkingspace.entity.Comment;
import com.datn.coworkingspace.entity.Space;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SpaceRepository extends JpaRepository<Space, Long> {

    @Query("SELECT s from Space s WHERE s.category.id=?1")
    List<Space> findSpacesByCategoryId(Long categoryId);

    @Query("SELECT s FROM Space s WHERE lower(s.name) LIKE %?1%" )
    List<Space> search( String key);

    Page<Space> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT count(s) from Space s WHERE s.category.id=?1")
    Long countSpacesByCategoryId(Long categoryId);


    @Query(value = "SELECT s FROM Space s WHERE s.id IN :listId")
    Page<Space> findSpaceByIds(Collection<Long> listId, Pageable pagingSort);
}
