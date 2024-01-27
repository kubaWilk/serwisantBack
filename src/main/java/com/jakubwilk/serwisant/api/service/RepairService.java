package com.jakubwilk.serwisant.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakubwilk.serwisant.api.entity.jpa.Cost;
import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface RepairService {
    Repair findById(int id);
    Repair findById(int id, Principal principal);

    List<Repair> findAllRepairs();

    Repair saveRepair(JsonNode repairJsonNode);

    Repair updateRepair(Repair repair);

    Repair updateRepairStatus(Map<String, String> toUpdate);
    void deleteCostFromARepair(int repairId, Cost theCost);

    void deleteRepair(int id);

    List<Repair> findAllRepairsByCustomerId(int id);

    List<Repair> findAllRepairsByDeviceId(int deviceId);

    List<Repair> findAllCustomerRepairs(JwtAuthenticationToken authentication);

    void acceptCosts(int repairId, JwtAuthenticationToken token);
}
