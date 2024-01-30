package com.jakubwilk.serwisant.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakubwilk.serwisant.api.entity.jpa.User;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserService {
    User findById(int id);
    User findByEmail(String email);
    List<User> findAll();
    User save(User user);
    User updateUserDetails(int id, User user);
    void delete(int id);
    void changePassword(String email, String password);
    User findByUsername(String username);
    Set<User> searchUser(Map<String, String> userToSearch);

    List<User> findAllCustomers();

    User getInfoAboutUser(Principal theUser);

    User updateUser(int userId, JsonNode node);
}
