package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.dao.UserRepository;
import com.jakubwilk.serwisant.api.entity.User;
import jakarta.transaction.Transactional;
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
            throw new RuntimeException("Did not find User with id of: " + id);
        }

        return user;
    }

    @Override
    public User findByIdWithUserDetails(int id) {
        return userRepository.findByIdAndFetchUserDetailsEagerly(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAllWithUserDetails() {
        return userRepository.findAllFetchUserDetailsEagerly();
    }

    @Override
    @Transactional
    public User save(User user) {
        userRepository.saveAndFlush(user);
        return user;
    }

    @Override
    @Transactional
    public User update(User user) {
        userRepository.saveAndFlush(user);
        return user;
    }

    @Override
    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);
    }
}
