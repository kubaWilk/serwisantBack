package com.jakubwilk.serwisant.api;

import com.jakubwilk.serwisant.api.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
public class UserControllerTests {

    @Autowired
    private UserController userController;

    @Test
    void contextLoads() throws Exception{
        assertThat(userController).isNotNull();
    }
}
