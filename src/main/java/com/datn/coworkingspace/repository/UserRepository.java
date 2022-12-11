package com.datn.coworkingspace.repository;

import com.datn.coworkingspace.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String username);

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);

    Page<User> findByIsAccCustomer(Boolean isAccCustomer, Pageable pageable);

    Page<User> findByUserNameContainingAndIsAccCustomer(String userName, Boolean isAccCustomer, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.isAccCustomer = false")
    List<User> findAllEmployee();

    @Query("SELECT u FROM User u WHERE u.isAccCustomer=false AND u.id=?1")
    Optional<User> findByIdEmployee(Long theId);

    @Query("SELECT count(u) FROM User u WHERE u.isAccCustomer=false")
    Long countEmployee();

    @Query("SELECT u FROM User u WHERE u.isAccCustomer = true")
    List<User> findAllCustomer();

    @Query("SELECT u FROM User u WHERE u.isAccCustomer=true AND u.id=?1")
    Optional<User> findByIdCustomer(Long theId);

    @Query("SELECT count(u) FROM User u WHERE u.isAccCustomer=true")
    Long countCustomer();

    Optional<User> findByEmail(String email);

    Optional<User> findByUserNameAndIsAccCustomer(String username, Boolean isAccCustomer);

    Page<User> findByEnabledAndIsAccCustomer(Integer enabled, Boolean b, Pageable pagingSort);

    Page<User> findByUserNameContainingAndEnabledAndIsAccCustomer(String userName, Integer enabled, Boolean isAccCustomer, Pageable pageable);

}
