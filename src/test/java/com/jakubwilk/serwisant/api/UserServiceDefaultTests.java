package com.jakubwilk.serwisant.api;

import com.jakubwilk.serwisant.api.dao.UserRepository;
import com.jakubwilk.serwisant.api.entity.User;
import com.jakubwilk.serwisant.api.entity.UserDetails;
import com.jakubwilk.serwisant.api.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    private User testUser;

    @BeforeEach
    public void setupTestData(){
        testUser = User.builder()
                .username("test")
                .password("test")
                .isActive(true)
                .email("test@test.com")
                .userDetails(UserDetails.builder()
                        .firstName("test")
                        .lastName("test")
                        .street("test")
                        .postCode("test")
                        .city("test")
                        .user(testUser)
                        .build())
                .build();

        User repoUser = userRepository.saveAndFlush(testUser);
    }

    @AfterEach()
    public void cleanup(){
        userRepository.delete(testUser);
    }

    @Test
    public void shouldReturnAUserById(){
        User foundUser = userService.findById(testUser.getId());

        assertThat(foundUser).usingRecursiveComparison()
                .ignoringFields("userDetails")
                .isEqualTo(testUser);
    }

    @Test
    public void shouldThrowRuntimeExceptionOnNotExistingUser(){
        int theId = testUser.getId();
        userRepository.delete(testUser);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.findById(theId);
        });
        String expectedMessage = "Did not find User with id of: " + theId;

        assertEquals(exception.getMessage(), expectedMessage);
    }
}
