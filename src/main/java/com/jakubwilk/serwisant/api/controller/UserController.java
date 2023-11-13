package com.jakubwilk.serwisant.api.controller;

import com.jakubwilk.serwisant.api.controller.AuthController;
import com.jakubwilk.serwisant.api.entity.User;
import com.jakubwilk.serwisant.api.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/user")
@AllArgsConstructor
@ControllerAdvice
public class UserController {
    private UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable("id") int id){
        User result = userService.findById(id);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> findAllUsers(){
        List<User> result = userService.findAll();

        return ResponseEntity.ok(result);
    }

    @PostMapping("/")
    public ResponseEntity<User> saveUser(@RequestBody User toSave){
        User user = userService.save(toSave);

        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<User> updateUser(@RequestBody User toSave){
        User user = userService.update(toSave);

        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int id){
        userService.delete(id);

        return ResponseEntity.ok("User with id :" + id + " deleted");
    }

    @GetMapping("/me")
    public String returnUserLoggedIn(Principal principal){
        return principal.toString();
//            return "hello" + principal.getName();
    }
}


