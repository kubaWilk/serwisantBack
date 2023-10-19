package com.jakubwilk.serwisant.api.dao;

import com.jakubwilk.serwisant.api.entity.User;
import com.jakubwilk.serwisant.api.entity.UserDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl implements UserDAO{
    private EntityManager entityManager;

    public UserDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User findById(int id) {
//        TypedQuery<User> query = entityManager.createQuery("FROM User u WHERE u.id=:data", User.class);
//        query.setParameter(id, "data");
//
//        return query.getSingleResult();
        return entityManager.find(User.class, id);
    }

    @Override
    @Transactional
    public void createUser(User user) {
        entityManager.persist(user);
    }


}
