package com.jakubwilk.serwisant.api.repository;

import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepairRepository extends JpaRepository<Repair, Integer> {

    @Query("SELECT r FROM Repair r WHERE r.issuer.id = ?1")
    List<Repair> findAllByIssuerId(int customerId);

    @Query("SELECT r FROM Repair r WHERE r.device.id = ?1")
    List<Repair> findAllByDeviceId(int deviceId);
}
