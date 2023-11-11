package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.exception.UserNotFoundException;
import com.jakubwilk.serwisant.api.dao.UserRepository;
import com.jakubwilk.serwisant.api.entity.User;
import com.jakubwilk.serwisant.api.service.event.events.UserRegisteredEvent;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.jakubwilk.serwisant.api.entity.Role.ROLE_CUSTOMER;

@Service
public class UserServiceDefault implements UserService{
    private UserRepository userRepository;
    private ApplicationEventPublisher eventPublisher;
    private PasswordEncoder passwordEncoder;

    public UserServiceDefault(UserRepository userRepository, ApplicationEventPublisher eventPublisher, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.passwordEncoder = passwordEncoder;
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
    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);

        if(user == null)
            throw new UserNotFoundException("User with email " + email + "was not found.");

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
        if(user == null) throw new IllegalArgumentException("User can't be null!");
        if(doesUserExists(user)) throw new IllegalArgumentException("User already exists!");

        User persisted;

        try{
            user.setRoles(ROLE_CUSTOMER);
            user.setActive(true);

            persisted = userRepository.saveAndFlush(user);

            UserRegisteredEvent newUserEvent = new UserRegisteredEvent(user,
                    "New user registered");
            eventPublisher.publishEvent(newUserEvent);
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

    @Override
    public void changePassword(String email, String password) {
        User user = userRepository.findByEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);
    }

    private boolean doesUserExists(User user){
        return userRepository.findById(user.getId()).isPresent();
    }

    private boolean doesUserExists(int id){
        Optional<User> result = userRepository.findById(id);
        return result.isPresent();
    }
}
