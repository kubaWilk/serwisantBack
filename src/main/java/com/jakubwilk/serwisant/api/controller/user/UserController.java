package com.jakubwilk.serwisant.api.controller.user;

import com.jakubwilk.serwisant.api.entity.jpa.Authority;
import com.jakubwilk.serwisant.api.entity.jpa.User;
import com.jakubwilk.serwisant.api.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/user")
@AllArgsConstructor
@ControllerAdvice
public class UserController {
    private UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/{id}")
    @Secured("ROLE_CUSTOMER")
    public ResponseEntity<User> findUserById(@PathVariable("id") int id){
        User result = userService.findById(id);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/")
    @Secured("ROLE_EMPLOYEE")
    public ResponseEntity<List<User>> findAllUsers(){
        List<User> result = userService.findAll();

        return ResponseEntity.ok(result);
    }

    @PostMapping("/")
    @Secured("ROLE_EMPLOYEE")
    public ResponseEntity<User> saveUser(@RequestBody User toSave){
        User user = userService.save(toSave);

        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }

    @PutMapping("/")
    @Secured("ROLE_EMPLOYEE")
    public ResponseEntity<User> updateUser(@RequestBody User toSave){
        User user = userService.update(toSave);

        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }

    @PostMapping("/search")
    @Secured("ROLE_EMPLOYEE")
    public ResponseEntity<Set<User>> searchUser(@RequestBody Map<String,String> userToSearch){
        Set <User> result = userService.searchUser(userToSearch);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int id){
        userService.delete(id);

        return ResponseEntity.ok("User with id :" + id + " deleted");
    }
}


