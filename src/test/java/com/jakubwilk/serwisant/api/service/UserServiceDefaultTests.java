package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.repository.UserRepository;
import com.jakubwilk.serwisant.api.entity.jpa.User;
import com.jakubwilk.serwisant.api.entity.jpa.UserInfo;
import com.jakubwilk.serwisant.api.exception.UserNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ExtendWith(MockitoExtension.class)
public class UserServiceDefaultTests {

    @Autowired
    private UserServiceDefault userService;

    @Autowired
    private UserRepository userRepository;
    private User testUser;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setupTestData(){
        testUser = User.builder()
                .username("test")
                .password("test")
                .isActive(true)
                .email("test@test.com")
                .userInfo(UserInfo.builder()
                        .firstName("test")
                        .lastName("test")
                        .street("test")
                        .postCode("test")
                        .city("test")
                        .user(testUser)
                        .build())
                .build();

        userRepository.saveAndFlush(testUser);
    }

    @AfterEach()
    public void cleanup(){
        userRepository.delete(testUser);
    }

    @Test
    public void findByIdShouldReturnAUserById(){
        User expected = userRepository.findById(testUser.getId()).get();
        User actual = userService.findById(testUser.getId());

        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("userDetails")
                .ignoringFields("roles")
                .isEqualTo(expected);
    }

    @Test
    public void findByIdshouldThrowUserNotFoundExceptionOnNotExistingUser(){
        int theId = testUser.getId();
        userRepository.delete(testUser);

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.findById(theId);
        });
        String expectedMessage = "Did not find User with id of: " + theId;

        assertEquals(exception.getMessage(), expectedMessage);
    }

    @Test
    public void findAllShouldReturnAllUsers(){
        List<User> expected = userRepository.findAll();
        List<User> actual = userService.findAll();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void findALlShouldThrowUserNotFoundExceptionWhenNoUsersAreFound(){
        userRepository.deleteAll();

        Exception exception = assertThrows(UserNotFoundException.class, () ->{
            userService.findAll();
        });

        String expectedMessage = "Did not find any users, check database connection";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void saveShouldSaveTheUser(){
        User saveTestUser = User.builder()
                .username("test2")
                .password("test")
                .isActive(true)
                .email("test2@test.com")
                .userInfo(UserInfo.builder()
                        .firstName("test2")
                        .lastName("test")
                        .street("test")
                        .postCode("test")
                        .city("test")
                        .user(testUser)
                        .build())
                .build();

        userService.save(saveTestUser);
        assertThat(userRepository.findById(saveTestUser.getId())).isNotEqualTo(null);
    }

    @Test
    public void updateShouldUpdateUserData(){
        testUser.setUsername("newUserName");
//        userService.updateUserDetails(testUser);

        User temp = userRepository.findById(testUser.getId()).get();

        assertEquals(temp.getUsername(), testUser.getUsername());
    }

    @Test
    public void deleteShouldDeleteUser(){
        int theId = testUser.getId();
        userService.delete(testUser.getId());

        assertFalse(userRepository.findById(theId).isPresent());
    }
}
