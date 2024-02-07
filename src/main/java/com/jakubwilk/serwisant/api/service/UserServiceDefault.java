package com.jakubwilk.serwisant.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jakubwilk.serwisant.api.entity.Role;
import com.jakubwilk.serwisant.api.entity.jpa.*;
import com.jakubwilk.serwisant.api.event.events.UserRegisteredEvent;
import com.jakubwilk.serwisant.api.exception.UserNotFoundException;
import com.jakubwilk.serwisant.api.repository.NoteRepository;
import com.jakubwilk.serwisant.api.repository.RepairRepository;
import com.jakubwilk.serwisant.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static com.jakubwilk.serwisant.api.entity.Role.ROLE_CUSTOMER;

@Service
@Secured("ROLE_CUSTOMER")
public class UserServiceDefault implements UserService {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final NoteRepository noteRepository;
    private final RepairRepository repairRepository;

    public UserServiceDefault(UserRepository userRepository, ApplicationEventPublisher eventPublisher, PasswordEncoder passwordEncoder, RepairRepository repairRepository, NoteRepository noteRepository, RepairRepository repairRepository1) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.passwordEncoder = passwordEncoder;
        this.noteRepository = noteRepository;
        this.repairRepository = repairRepository1;
    }

    @Override
    public User findById(int id) {
        Optional<User> result = userRepository.findById(id);

        User user;

        if (result.isPresent()) {
            user = result.get();
        } else {
            throw new UserNotFoundException("Did not find User with id of: " + id);
        }

        return user;
    }

    @Override
    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null)
            throw new UserNotFoundException("User with email " + email + " was not found.");

        return user;
    }

    @Override
    public List<User> findAll() {
        return  userRepository.findAll();
    }

    @Override
    @Transactional
    public User save(User user) {
        if (user == null) throw new IllegalArgumentException("User can't be null!");
        if (user.getId() != 0) throw new IllegalArgumentException("User has to be provided with no ID");
        if (doesUserExistWithGivenUsername(user))
            throw new IllegalArgumentException("User with given username already exists!");
        if (doesUserExistWithGivenEMail(user))
            throw new IllegalArgumentException("User has to have a unique e-mail address!");

        user.setPassword("Startowe@");
        User persisted;

        try {
//            Authority newUser = new Authority(user, user.getUsername(), ROLE_CUSTOMER);
            user.addRole(ROLE_CUSTOMER);
            user.setActive(true);

            persisted = userRepository.saveAndFlush(user);

            UserRegisteredEvent newUserEvent = new UserRegisteredEvent(UserServiceDefault.class,
                    persisted, user.getPassword());

            eventPublisher.publishEvent(newUserEvent);
        } catch (DataIntegrityViolationException exception) {
            throw new IllegalArgumentException(
                    "User has to be provided with no ID, unique username and unique e-mail"
            );
        }

        return persisted;
    }

    private boolean doesUserExistWithGivenUsername(User user) {
        User userNameCheck = userRepository.findByUsername(user.getUsername());
        return userNameCheck != null;
    }

    private boolean doesUserExistWithGivenEMail(User user) {
        User emailCheck = userRepository.findByEmail(user.getEmail());
        return emailCheck != null;
    }

    @Override
    @Transactional
    public User updateUserDetails(int id, User user) {
        Optional<User> result = userRepository.findById(id);

        if (result.isPresent()) {
            User toUpdate = result.get();

            toUpdate.setUserInfo(user.getUserInfo());
            return userRepository.saveAndFlush(toUpdate);
        } else {
            throw new UserNotFoundException("Can't find user with id: " + id);
        }
    }

    @Override
    @Transactional
    public void delete(int id) {
        Optional<User> result = userRepository.findUserWithRepairsById(id);
        if (result.isEmpty()) throw new UserNotFoundException("User with id: " + id + " not found!");

        User user = result.get();

        List<Note> notesToDelete = new ArrayList<>(user.getNotes());
        List<Repair> repairsToDelete = new ArrayList<>(user.getRepairs());

        for(Note note : notesToDelete){
            noteRepository.delete(note);
        }

        for(Repair repair : repairsToDelete){
            repairRepository.delete(repair);
        }

        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void changePassword(String email, String password) {
        User user = userRepository.findByEmail(email);
        String encoded = passwordEncoder.encode(password);
        user.setPassword(encoded);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(String email, String oldPassword, String password) {
        User user = userRepository.findByEmail(email);
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            String encoded = passwordEncoder.encode(password);
            user.setPassword(encoded);

            userRepository.save(user);
        } else throw new IllegalArgumentException("Old password doesn't match the one in the database!");
    }

    @Override
    public User findByUsername(String username) {
        User toReturn = userRepository.findByUsername(username);
        if (toReturn == null) {
            throw new UserNotFoundException("User with username: " + username + " not found");
        }

        return toReturn;
    }

    @Override
    public Set<User> searchUser(Map<String, String> userToSearch) {
        Set<User> result = new HashSet<>();

        if (userToSearch.containsKey("username")) {
            String property = userToSearch.get("username");
            User byUsername = userRepository.findByUsername(property);
            if (byUsername != null) {
                result.add(byUsername);
            }
        }

        if (userToSearch.containsKey("eMail")) {
            String property = userToSearch.get("eMail");
            User byEmail = userRepository.findByEmail(property);
            if (byEmail != null) {
                result.add(byEmail);
            }
        }
        return result;
    }

    @Override
    public List<User> findAllCustomers() {
        return userRepository.findAllCustomers();
    }

    @Override
    public User getInfoAboutUser(Principal theUser) {
        return userRepository.findByUsername(theUser.getName());
    }

    @Override
    @Transactional
    public User updateUser(int id, JsonNode node) {
        User user;
        Optional<User> result = userRepository.findById(id);
        if (result.isEmpty()) throw new UserNotFoundException("User with id: " + id + " not found.");

        user = result.get();
        UserInfo userInfo = user.getUserInfo();
        JsonNode userInfoJson = node.get("userInfo");

        userInfo.setFirstName(userInfoJson.get("firstName").asText());
        userInfo.setLastName(userInfoJson.get("lastName").asText());
        userInfo.setPhoneNumber(userInfoJson.get("phoneNumber").asText());
        userInfo.setStreet(userInfoJson.get("street").asText());
        userInfo.setPostCode(userInfoJson.get("postCode").asText());
        userInfo.setCity(userInfoJson.get("city").asText());
        user.setUserInfo(userInfo);

        ArrayNode rolesJSON = (ArrayNode) node.get("roles");

        Set<Role> obtainedRoles = new HashSet<>();
        for (var element : rolesJSON) {
            Role role = Role.valueOf(element.asText());
            obtainedRoles.add(role);
        }

        Set<Authority> roles = user.getAuthoritySet();

        obtainedRoles.stream()
                .filter(role -> roles.stream().noneMatch(authority -> authority.getAuthority() == role))
                .forEach(role -> roles.add(new Authority(user, user.getUsername(), role)));

        Set<Authority> rolesToRemove = roles.stream()
                .filter(authority -> obtainedRoles.stream().noneMatch(role -> authority.getAuthority() == role))
                .collect(Collectors.toSet());

        rolesToRemove.forEach(element -> user.removeRole(element.getAuthority()));

        return userRepository.save(user);
    }
}