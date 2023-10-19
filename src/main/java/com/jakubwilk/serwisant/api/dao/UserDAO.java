package com.jakubwilk.serwisant.api.dao;

import com.jakubwilk.serwisant.api.entity.User;

public interface UserDAO {
    User findById(int id);
    void createUser(User user);
}
