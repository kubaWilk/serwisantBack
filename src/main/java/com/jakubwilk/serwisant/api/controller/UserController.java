package com.jakubwilk.serwisant.api.controller;

import com.jakubwilk.serwisant.api.dao.UserDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private UserDAO userDAO;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("test")
    public String getUser(){
        return userDAO.findById(1).toString();
    }
}
