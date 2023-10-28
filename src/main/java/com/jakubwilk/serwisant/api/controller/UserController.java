package com.jakubwilk.serwisant.api.controller;

import com.jakubwilk.serwisant.api.dao.UserRepository;
import com.jakubwilk.serwisant.api.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

import java.util.Optional;


@RestController
@RequestMapping("/v1/user")
public class UserController {
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @GetMapping("/{id}")
    public ResponseEntity<String> findUserById(@PathVariable("id") int id){
        Optional<User> result = userRepository.findById(id);

        if(result.isPresent()){
            return ResponseEntity.ok(result.get().toString());
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/me")
    public String returnUserLoggedIn(Principal principal){
            return "hello" + principal.getName();
    }
}


