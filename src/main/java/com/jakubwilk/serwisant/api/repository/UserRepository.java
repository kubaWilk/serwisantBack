package com.jakubwilk.serwisant.api.repository;

import com.jakubwilk.serwisant.api.entity.jpa.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsername(String username);
    public User findByEmail(String email);
    @Query("SELECT u FROM User u JOIN Authority a ON u.username = a.username WHERE a.authority = 'ROLE_CUSTOMER'")
    public List<User> findAllCustomers();
}
