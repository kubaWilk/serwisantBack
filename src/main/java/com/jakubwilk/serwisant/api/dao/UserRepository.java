package com.jakubwilk.serwisant.api.dao;

import com.jakubwilk.serwisant.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u JOIN FETCH u.userDetails WHERE u.id = (:id)")
    User findByIdAndFetchUserDetailsEagerly(@Param("id") int id);

    @Query("SELECT u FROM User u JOIN FETCH u.userDetails")
    List<User> findAllFetchUserDetailsEagerly();
}
