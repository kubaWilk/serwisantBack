package com.jakubwilk.serwisant.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakubwilk.serwisant.api.entity.LoginRequest;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class AuthControllerTests {
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;
    @BeforeEach
    public void setup(){
        mapper = new ObjectMapper();
    }

    @Test
    public void loginWithValidCredentialsShouldReturnOkStatus() throws Exception {
        LoginRequest temp = new LoginRequest("root", "root");

        this.mockMvc.perform(post("/auth/login")
                .content(mapper.writeValueAsString(temp))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void loginWithInvalidCredentialsShouldReturnBadRequest() throws Exception {
        LoginRequest temp = new LoginRequest("root", "password");

        this.mockMvc.perform(post("/auth/login")
                        .content(mapper.writeValueAsString(temp))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void emptyBodyShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void invalidArgumentShouldReturnBadRequst() throws Exception {
        this.mockMvc.perform(post("/auth/login")
                .content("wrongData")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void validEmailAddressOnResetPasswdRequestShouldReturnOk() throws Exception {
        Map<String,String>  request = new HashMap<>();
        request.put("email", "test@test.com");

        this.mockMvc.perform(post("/auth/reset/request")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void invalidEmailAddressShouldReturnBadRequestWithMessageThatUserWasNotFound() throws Exception {
        Map<String,String>  request = new HashMap<>();
        request.put("email", "wrong@email.com");

        this.mockMvc.perform(post("/auth/reset/request")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    JSONObject res = new JSONObject(result.getResponse().getContentAsString());
                    String mes = res.getString("message");
                    assertThat(mes).contains("User with email " + request.get("email") + " was not found");
                });
    }

    @Test
    public void invalidResetRequestShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(post("/auth/reset/request")
                        .content(mapper.writeValueAsString("blabla"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void invalidTokenShouldReturnBadRequest() throws Exception {
        Map<String,String> mockNewPassword = new HashMap<>();
        mockNewPassword.put("firstPassword", "password");
        mockNewPassword.put("secondPassword", "password");
        this.mockMvc.perform(post("/auth/reset?token=failedToken")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(mockNewPassword)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    JSONObject res = new JSONObject(result.getResponse().getContentAsString());
                    String mes = res.getString("message");
                    assertThat(mes).contains("Could not find provided token or it's expired!");
                });
    }

    @Test
    public void invalidPasswordsShouldReturnBadRequestOnResetPasswd() throws Exception {
        Map<String,String> mockNewPassword = new HashMap<>();
        mockNewPassword.put("firstPassword", "password");
        mockNewPassword.put("secondPassword", "passwordNotMatching");

        this.mockMvc.perform(post("/auth/reset?token=failedToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(mockNewPassword)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    JSONObject res = new JSONObject(result.getResponse().getContentAsString());
                    String mes = res.getString("message");
                    System.out.println(mes);
                    assertThat(mes).contains("Provided passwords doesn't match!");
                });
    }

    @Test
    @WithMockUser(username="cust", password = "root")
    public void validPasswordsShouldReturnOk() throws Exception {
        Map<String,String> request = new HashMap<>();
        request.put("oldPassword", "root");
        request.put("newPassword", "root2");

        mockMvc.perform(put("/auth/change-password")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="cust2", password = "root")
    public void invalidPasswordsShouldReturnBadRequestOnChangePasswd() throws Exception {
        Map<String,String> request = new HashMap<>();
        request.put("oldPassword", "root123");
        request.put("newPassword", "root2");

        mockMvc.perform(put("/auth/change-password")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    JSONObject res = new JSONObject(result.getResponse().getContentAsString());
                    String mes = res.getString("message");
                    assertThat(mes).contains("Old password doesn't match the one in the database!");
                });
    }

    @Test
    @WithMockUser(username="root", password = "root")
    public void invalidRequestBodyShouldReturnBadRequestOnChangePasswd() throws Exception {
        Map<String,String> request = new HashMap<>();
        request.put("wrong", "body");

        mockMvc.perform(put("/auth/change-password")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    JSONObject res = new JSONObject(result.getResponse().getContentAsString());
                    String mes = res.getString("message");
                    assertThat(mes).contains("Invalid request body! You must provide both old and new password!");
                });
    }

}
