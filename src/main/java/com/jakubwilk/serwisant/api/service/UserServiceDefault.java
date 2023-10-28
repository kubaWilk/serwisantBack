package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.controller.user.UserNotFoundException;
import com.jakubwilk.serwisant.api.dao.UserRepository;
import com.jakubwilk.serwisant.api.entity.User;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceDefault implements UserService{
    private UserRepository userRepository;

    public UserServiceDefault(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(int id) {
        Optional<User> result = userRepository.findById(id);

        return handleUserOptional(result, id);
    }

    private User handleUserOptional(Optional<User> result, int id) {
        User user;

        if(result.isPresent()){
            user = result.get();
        }
        else{
            throw new UserNotFoundException("Did not find User with id of: " + id);
        }

        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> users = userRepository.findAll();

        if(users.isEmpty()){
            throw new UserNotFoundException("Did not find any users, check database connection");
        }
        else{
            return userRepository.findAll();
        }
    }

    @Override
    @Transactional
    public User save(@NotNull User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public User update(@NotNull User user) {
        userRepository.saveAndFlush(user);
        return user;
    }

    @Override
    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);
    }
}
