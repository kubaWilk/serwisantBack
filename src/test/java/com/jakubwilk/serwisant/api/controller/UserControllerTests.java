package com.jakubwilk.serwisant.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jakubwilk.serwisant.api.controller.user.UserController;
import com.jakubwilk.serwisant.api.entity.Role;
import com.jakubwilk.serwisant.api.entity.jpa.Authority;
import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import com.jakubwilk.serwisant.api.entity.jpa.User;
import com.jakubwilk.serwisant.api.entity.jpa.UserInfo;
import com.jakubwilk.serwisant.api.repository.UserRepository;
import com.jakubwilk.serwisant.api.service.UserService;
import jakarta.transaction.Transactional;
import org.checkerframework.checker.units.qual.A;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    User testUser;

    ObjectMapper mapper;

    @Test
    void contextLoads() throws Exception{
        assertThat(userController).isNotNull();
    }

    @BeforeEach
    public void setUp(){
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        testUser = User.builder()
                .username("test")
                .password("test")
                .isActive(true)
                .email("anothertest@test.com")
                .userInfo(UserInfo.builder()
                        .firstName("test")
                        .lastName("test")
                        .street("test")
                        .postCode("test")
                        .city("test")
                        .user(testUser)
                        .build())
                .createdDate(LocalDateTime.now())
                .build();

        Set<Authority> roles = new HashSet<>();
        roles.add(new Authority(testUser, testUser.getUsername(), Role.ROLE_CUSTOMER));
        testUser.setRoles(roles);

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
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    JSONObject res = new JSONObject(result.getResponse().getContentAsString());
                    String message = res.getString("message");
                    assertThat(message).contains("Did not find User with id of:");
                });
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
    void postShouldSaveTheUser() throws Exception {
        User newUser = User.builder()
                .username("UniqueUserName")
                .password("test")
                .isActive(true)
                .email("UniqueE-Mail@test.com")
                .userInfo(UserInfo.builder()
                        .firstName("test")
                        .lastName("test")
                        .street("test")
                        .postCode("test")
                        .city("test")
                        .user(testUser)
                        .build())
                .roles(new HashSet<>())
                .build();

        this.mockMvc.perform(post("/user/")
                .with(jwt())
                .content(new ObjectMapper().writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated());

        assertThat(userRepository.findById(newUser.getId())).isNotNull();
    }

    @Test
    void putShouldUpdateUser() throws Exception{
        String newFirstName = "changedFirstName";
        User request = User.builder()
                .id(testUser.getId())
                .username(testUser.getUsername())
                .password(testUser.getPassword())
                .email(testUser.getEmail())
                .userInfo(testUser.getUserInfo())
                .roles(new HashSet<>())
                .build();
        UserInfo updatedUserInfo = request.getUserInfo();
        updatedUserInfo.setFirstName(newFirstName);
        request.setUserInfo(updatedUserInfo);

        this.mockMvc.perform(put("/user/" + testUser.getId())
                .with(jwt())
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated());

        Optional<User> check = userRepository.findById(testUser.getId());
        assertThat(check.get().getUserInfo().getFirstName()).isEqualTo(newFirstName);
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
