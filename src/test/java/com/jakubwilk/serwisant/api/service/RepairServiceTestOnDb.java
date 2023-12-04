package com.jakubwilk.serwisant.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakubwilk.serwisant.api.dao.RepairRepository;
import com.jakubwilk.serwisant.api.entity.Device;
import com.jakubwilk.serwisant.api.entity.Repair;
import com.jakubwilk.serwisant.api.entity.User;
import com.jakubwilk.serwisant.api.entity.UserDetails;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@AutoConfigureTestDatabase
public class RepairServiceTestOnDb {
    @Autowired
    private RepairServiceDefault repairService;

    @Autowired
    private RepairRepository repairRepository;

    @Autowired
    private ObjectMapper mapper;

    private Repair testRepair;

    @BeforeEach
    void setup(){
        testRepair = Repair.builder()
                .issuer(User
                        .builder()
                        .userDetails(new UserDetails())
                        .build())
                .device(new Device())
                .build();

        repairRepository.save(testRepair);
    }

    @AfterEach
    void cleanUp(){
        repairRepository.delete(testRepair);
    }
    @Test
    public void saveRepairShouldSaveRepair(){
        Map<String,Integer> expected = new HashMap<>();
        expected.put("issuer", testRepair.getIssuer().getId());
        expected.put("device", testRepair.getDevice().getId());

        JsonNode testNode = mapper.convertValue(expected, JsonNode.class);
        Repair actual = repairService.saveRepair(testNode);

        assertEquals(expected.get("device"),
                actual.getDevice().getId());
        assertEquals(expected.get("issuer"),
                actual.getIssuer().getId());
    }

    @Test
    public void updateShouldUpdateRepair(){
        testRepair.setDevice(Device.builder().build());
        Repair actual = repairService.updateRepair(testRepair);

        assertThat(actual).usingRecursiveComparison().isEqualTo(testRepair);
    }

    @Test
    public void deleteShouldDeleteRepair(){
        int theId = testRepair.getId();
        repairService.deleteRepair(theId);
        Optional<Repair> actual = repairRepository.findById(theId);

        assertFalse(actual.isPresent());
    }
}
