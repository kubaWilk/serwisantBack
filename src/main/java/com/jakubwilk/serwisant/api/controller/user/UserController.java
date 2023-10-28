package com.jakubwilk.serwisant.api.controller.user;

import com.jakubwilk.serwisant.api.entity.User;
import com.jakubwilk.serwisant.api.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/v1/user")
@AllArgsConstructor
@ControllerAdvice
public class UserController {
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable("id") int id){
        User result = userService.findById(id);

        return ResponseEntity.ok(result);
    }

//    @GetMapping
//    public ResponseEntity<List<User>> findAllUsers(){
//
//    }

    @GetMapping("/me")
    public String returnUserLoggedIn(Principal principal){
            return "hello" + principal.getName();
    }
}


