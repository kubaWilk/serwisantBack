package com.jakubwilk.serwisant.api.dao;

import com.jakubwilk.serwisant.api.entity.Repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepairRepository extends JpaRepository<Repair, Integer> {
    @Query("SELECT r FROM Repair r " +
            "JOIN FETCH r.issuer JOIN FETCH r.device")
    List<Repair> findAllReapirsEagerly();
}
