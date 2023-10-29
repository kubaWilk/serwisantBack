package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.exception.UserAlreadyExistsException;
import com.jakubwilk.serwisant.api.exception.UserNotFoundException;
import com.jakubwilk.serwisant.api.dao.UserRepository;
import com.jakubwilk.serwisant.api.entity.User;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
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
    public User save(User user) {
        if(doesUserExists(user)) throw new IllegalArgumentException("User already exists!");
        User persisted;

        try{
            persisted = userRepository.saveAndFlush(user);
        }catch(DataIntegrityViolationException exception){
            throw new IllegalArgumentException(
                    "User has to be provided with no ID, unique username and unique e-mail"
            );
        }


        return persisted;
    }

    @Override
    @Transactional
    public User update(User user) {
        if(user.getId() == 0) throw new IllegalArgumentException("Provide user id!");

        if(!doesUserExists(user)) {
            throw new IllegalArgumentException("Can't find user with id: " + user.getId());
        }

        return userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public void delete(int id) {
        if(!doesUserExists(id)){
            throw new UserNotFoundException("Can't find user with id: " + id);
        }

        userRepository.deleteById(id);
    }

    private boolean doesUserExists(User user){
        return userRepository.findById(user.getId()).isPresent();
    }

    private boolean doesUserExists(int id){
        Optional<User> result = userRepository.findById(id);
        return result.isPresent();
    }
}
