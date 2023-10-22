package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.entity.User;

import java.util.List;

public interface UserService {
    User findById(int id);
    User findByIdWithUserDetails(int id);
    List<User> findAll();
    List<User> findAllWithUserDetails();
    User save(User user);
    User update(User user);
    void delete(int id);
}
