package com.jakubwilk.serwisant.api.dao;

import com.jakubwilk.serwisant.api.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    public PasswordResetToken findByUserId(int id);
}
