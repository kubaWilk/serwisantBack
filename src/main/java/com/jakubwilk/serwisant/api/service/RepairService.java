package com.jakubwilk.serwisant.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakubwilk.serwisant.api.entity.Repair;

import java.util.List;

public interface RepairService {
    Repair findById(int id);

    List<Repair> findAllRepairs();

    Repair saveRepair(JsonNode repairJsonNode);

    Repair updateRepair(Repair repair);

    void deleteRepair(int id);
}
