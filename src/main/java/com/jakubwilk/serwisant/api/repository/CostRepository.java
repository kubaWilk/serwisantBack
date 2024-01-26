package com.jakubwilk.serwisant.api.repository;

import com.jakubwilk.serwisant.api.entity.jpa.Cost;
import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface CostRepository extends JpaRepository<Cost, Integer> {
    List<Cost> findAllByRepairId(int repairId);

    @Query("SELECT c FROM Cost c WHERE FUNCTION('DATE', c.createdDate) BETWEEN ?1 AND ?2 ORDER BY c.createdDate ASC")
    List<Cost> findAllByTimeInterval(LocalDate startDate, LocalDate endDate);
}
