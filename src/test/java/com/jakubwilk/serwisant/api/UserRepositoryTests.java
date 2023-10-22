package com.jakubwilk.serwisant.api;

import com.jakubwilk.serwisant.api.dao.UserRepository;
import com.jakubwilk.serwisant.api.entity.User;
import com.jakubwilk.serwisant.api.entity.UserDetails;
import jakarta.persistence.TypedQuery;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
public class UserRepositoryTests {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager entityManager;

    private User testUser;
    private UserDetails testUserDetails;

    @BeforeEach
    public void setupTestData(){
        testUserDetails = UserDetails.builder()
                .firstName("test")
                .lastName("test")
                .street("test")
                .postCode("test")
                .city("test")
                .user(testUser)
                .build();

        testUser = User.builder()
                .username("test")
                .password("test")
                .isActive(true)
                .email("test@test.com")
                .userDetails(testUserDetails)
        .build();
    }

    @Test
    public void shouldFindUserById(){
        entityManager.persist(testUser);
        Optional<User> result = userRepository.findById(testUser.getId());

        assertThat(result.get())
                .isEqualTo(entityManager.find(User.class, testUser.getId()));
    }

    @Test
    public void shouldReturnAListOfUsers(){
        entityManager.persist(testUser);
        TypedQuery<User> query = entityManager.getEntityManager()
                .createQuery("SELECT u FROM User u", User.class);

        List<User> emUsers = query.getResultList();
        List<User> repoUsers = userRepository.findAll();

        assertEquals(emUsers, repoUsers);
    }

    @Test
    public void shouldReturnAListOfUsersWithUserDetails(){
        entityManager.persist(testUser);
        TypedQuery<User> query = entityManager.getEntityManager()
                .createQuery("SELECT u FROM User u " +
                        "JOIN FETCH u.userDetails", User.class);

        List<User> emUsers = query.getResultList();
        List<User> repoUsers = userRepository.findAllFetchUserDetailsEagerly();

        assertEquals(emUsers, repoUsers);
    }

    @Test
    public void shouldReturnUserWithUserDetailsById(){
        entityManager.persist(testUser);
        User repoUser = userRepository.findByIdAndFetchUserDetailsEagerly(testUser.getId());
        assertThat(repoUser.getUserDetails()).isEqualTo(testUserDetails);
    }

    @Test
    public void shouldSaveUser(){
        User savedUser = userRepository.save(testUser);
        assertThat(entityManager.find(User.class, savedUser.getId())).isEqualTo(testUser);
    }

    @Test
    public void shouldUpdateUser(){
        entityManager.persist(testUser);
        testUser.setUsername("test2");
        User updatedUser = userRepository.saveAndFlush(testUser);
        assertEquals(testUser.getUsername(), updatedUser.getUsername());
    }

    @Test
    public void shouldDeleteUser(){
        entityManager.persist(testUser);
        int id = testUser.getId();
        userRepository.delete(testUser);
        User shouldBeNull = entityManager.find(User.class, id);

        assertNull(shouldBeNull);
    }
}
