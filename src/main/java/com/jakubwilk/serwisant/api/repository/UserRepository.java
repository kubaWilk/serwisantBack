package com.jakubwilk.serwisant.api.repository;

import com.jakubwilk.serwisant.api.entity.jpa.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsername(String username);
    public User findByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.repairs WHERE u.id = :userId")
    Optional<User> findUserWithRepairsById(@Param("userId") int userId);
    @Query("SELECT u FROM User u JOIN Authority a ON u.username = a.username WHERE a.authority = 'ROLE_CUSTOMER'")
    public List<User> findAllCustomers();
}
