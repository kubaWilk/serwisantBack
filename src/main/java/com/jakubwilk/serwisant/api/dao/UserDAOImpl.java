package com.jakubwilk.serwisant.api.dao;

import com.jakubwilk.serwisant.api.entity.User;
import com.jakubwilk.serwisant.api.entity.UserDetails;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl implements UserDAO{
    private EntityManager entityManager;

    public UserDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void createUser(User user) {
        entityManager.persist(user);
    }
}
