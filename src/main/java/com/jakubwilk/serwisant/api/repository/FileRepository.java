package com.jakubwilk.serwisant.api.repository;

import com.jakubwilk.serwisant.api.entity.jpa.RepairDbFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FileRepository extends JpaRepository<RepairDbFile, UUID> {
    @Query("SELECT f FROM RepairDbFile f where f.repairId = ?1")
    List<RepairDbFile> findAllByRepairId(int repairId);

    @Query("DELETE FROM RepairDbFile f where f.repairId = ?1")
    void deleteAllByRepairId(int repairId);
}
