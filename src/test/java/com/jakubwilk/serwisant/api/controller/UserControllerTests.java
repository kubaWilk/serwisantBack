package com.jakubwilk.serwisant.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakubwilk.serwisant.api.controller.UserController;
import com.jakubwilk.serwisant.api.dao.UserRepository;
import com.jakubwilk.serwisant.api.entity.User;
import com.jakubwilk.serwisant.api.entity.UserDetails;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    User testUser;

    @Test
    void contextLoads() throws Exception{
        assertThat(userController).isNotNull();
    }

    @BeforeEach
    public void setUp(){
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

        userRepository.saveAndFlush(testUser);
    }

    @AfterEach
    void cleanUp(){
        userRepository.delete(testUser);
    }

    @Test
    void findByIdShouldReturnAUser() throws Exception {
        this.mockMvc.perform(get("/user/" + testUser.getId())
                        .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testUser.getUsername())));
    }

    @Test
    void findByIdShouldReturnNotFoundIfNoUser() throws Exception {
        userRepository.delete(testUser);

        this.mockMvc.perform(get("/user/" + testUser.getId())
                        .with(jwt()))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllShouldReturnAllUsers() throws Exception {
        this.mockMvc.perform(get("/user/")
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testUser.getUsername())))
                .andExpect(content().string(containsString("root")));
    }

    @Test
    void findAllShouldThrowUserNotFoundExceptionWhenNoUsers() throws Exception {
        userRepository.deleteAll();

        this.mockMvc.perform(get("/user/")
                        .with(jwt()))
                .andExpect(status().isNotFound());
    }

    @Test
    void postShouldSaveTheUser() throws Exception {
        User newUser = User.builder()
                .username("UniqueUserName")
                .password("test")
                .isActive(true)
                .email("UniqueE-Mail@test.com")
                .userDetails(UserDetails.builder()
                        .firstName("test")
                        .lastName("test")
                        .street("test")
                        .postCode("test")
                        .city("test")
                        .user(testUser)
                        .build())
                .build();

        userRepository.delete(newUser);

        this.mockMvc.perform(post("/user/")
                .with(jwt())
                .content(new ObjectMapper().writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated());

        assertThat(userRepository.findById(newUser.getId())).isNotNull();
    }

//    @Before
//    void removeRolesfromUser(){
//        userRepository.delete(testUser);
//        testUser
//    }
    @Test
    void putShouldUpdateUser() throws Exception{
        String userName = "newUserName";
        testUser.setUsername(userName);
        this.mockMvc.perform(put("/user/")
                .with(jwt())
                .content(new ObjectMapper().writeValueAsString(testUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated());
        Optional<User> check = userRepository.findById(testUser.getId());
        assertThat(check.get().getUsername()).isEqualTo(userName);
    }
    @Test
    void getShouldNotReturnUsersPassword(){

    }

    @Test
    void deleteShouldDeleteUser() throws Exception{
        int theId = testUser.getId();
        this.mockMvc.perform(delete("/user/" + theId)
                        .with(jwt())
                )
                .andExpect(status().isOk());
        Optional<User> check = userRepository.findById(theId);
        assertThat(check.isPresent()).isFalse();
    }



}
