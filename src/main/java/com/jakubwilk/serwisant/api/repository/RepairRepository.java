package com.jakubwilk.serwisant.api.repository;

import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RepairRepository extends JpaRepository<Repair, Integer> {

    @Query("SELECT r FROM Repair r WHERE r.issuer.id = ?1")
    List<Repair> findAllByIssuerId(int customerId);

    @Query("SELECT r FROM Repair r WHERE r.device.id = ?1")
    List<Repair> findAllByDeviceId(int deviceId);

    @Query("SELECT r FROM Repair r WHERE FUNCTION('DATE', r.createdDate) BETWEEN ?1 AND ?2 ORDER BY r.createdDate ASC")
    List<Repair> findAllOpenByTimeInterval(LocalDate startDate, LocalDate endDate);

    @Query("SELECT r FROM Repair r WHERE FUNCTION('DATE', r.closedAt) BETWEEN ?1 AND ?2 AND r.repairStatus ='CLOSED' ORDER BY r.closedAt ASC")
    List<Repair> findAllClosedByTimeInterval(LocalDate startDate, LocalDate endDate);
}
