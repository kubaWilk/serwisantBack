package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.entity.jpa.User;

import java.util.List;

public interface UserService {
    User findById(int id);
    User findByEmail(String email);
    List<User> findAll();
    User save(User user);
    User update(User user);
    void delete(int id);
    void changePassword(String email, String password);
    User findByUsername(String username);
}
