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
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
public class RepairServiceTests {
    @Autowired
    private RepairService repairService;

    @Autowired
    private RepairRepository repairRepository;

    @Autowired
    ObjectMapper mapper = new ObjectMapper();

    private Repair testRepair;

    @BeforeEach
    @Transactional
    void setUp(){
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
    @Transactional
    void cleanUp(){
        repairRepository.delete(testRepair);
    }

    @Test
    void findByIdShouldReturnARepair(){
        Repair expected = repairRepository.findById(testRepair.getId()).get();
        Repair actual = repairService.findById(testRepair.getId());

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void findByIdShouldThrowAnExceptionOnNotFound(){
        int theId = testRepair.getId();
        repairRepository.delete(testRepair);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            repairService.findById(theId);
        });

        String expectedMessage = "No repair with id: " + theId;
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void findAllShouldReturnAllUsers(){
        List<Repair> expected = repairRepository.findAll();
        List<Repair> actual = repairService.findAllRepairs();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void findALlShouldThrowUserNotFoundExceptionWhenNoUsersAreFound(){
        repairRepository.deleteAll();

        Exception exception = assertThrows(RuntimeException.class, () ->{
            repairService.findAllRepairs();
        });

        String expectedMessage = "No repairs found!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
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
    public void saveShouldThrowNullPointerExceptionOnNullRepair(){
        Exception exception = assertThrows(NullPointerException.class, () ->{
            repairService.saveRepair(null);
        });
        String expectedMessage = "Repair can't be null!";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionOnNoIssuerField(){
         Map<String,Integer> testData = new HashMap<>();
         JsonNode jsonNode = mapper.convertValue(testData, JsonNode.class);

         Exception exception = assertThrows(IllegalArgumentException.class, () -> {
             repairService.saveRepair(jsonNode);
         });

        String expectedMessage = "Repair must containt issuer's ID!";
        String actualMessage = exception.getMessage();
    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionOnNoDeviceField(){
        Map<String,Integer> testData = new HashMap<>();
        testData.put("issuer", 1);
        JsonNode jsonNode = mapper.convertValue(testData, JsonNode.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            repairService.saveRepair(jsonNode);
        });

        String expectedMessage = "Repair must contain device's ID!";
        String actualMessage = exception.getMessage();
    }

    @Test
    public void updateShouldUpdateRepair(){
        testRepair.setDevice(Device.builder().build());
        Repair actual = repairService.updateRepair(testRepair);

        assertThat(actual).usingRecursiveComparison().isEqualTo(testRepair);
    }

    @Test
    public void updateShouldThrowNullPointerExceptionOnNullRepair(){
        Exception exception = assertThrows(NullPointerException.class, () ->{
            repairService.updateRepair(null);
        });
        String expectedMessage = "Repair can't be null!";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void deleteShouldDeleteRepair(){
        int theId = testRepair.getId();
        repairService.deleteRepair(theId);
        Optional<Repair> actual = repairRepository.findById(theId);

        assertFalse(actual.isPresent());
    }
}
