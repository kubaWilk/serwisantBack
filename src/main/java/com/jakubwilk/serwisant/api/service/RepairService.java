package com.jakubwilk.serwisant.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakubwilk.serwisant.api.entity.jpa.Cost;
import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import jakarta.transaction.Transactional;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface RepairService {
    Repair findById(int id);

    List<Repair> findAllRepairs();

    Repair saveRepair(JsonNode repairJsonNode);

    Repair updateRepair(Repair repair);

    Repair updateRepairStatus(Map<String, String> toUpdate);
    void deleteCostFromARepair(int repairId, Cost theCost);

    void deleteRepair(int id);
}
