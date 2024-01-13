package com.jakubwilk.serwisant.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakubwilk.serwisant.api.repository.DeviceRepository;
import com.jakubwilk.serwisant.api.repository.RepairRepository;
import com.jakubwilk.serwisant.api.repository.UserRepository;
import com.jakubwilk.serwisant.api.entity.jpa.Device;
import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import com.jakubwilk.serwisant.api.entity.jpa.User;
import com.jakubwilk.serwisant.api.entity.jpa.UserInfo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase
@ExtendWith(MockitoExtension.class)
public class RepairServiceTests {
    private RepairServiceDefault repairService;
    private RepairRepository repairRepository;
    private UserRepository userRepository;
    private UserService userService;
    private DeviceService deviceService;
    private DeviceRepository deviceRepository;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    ObjectMapper mapper = new ObjectMapper();

    private Repair testRepair;

    @BeforeEach
    @Transactional
    void setUp(){
        repairRepository = Mockito.mock(RepairRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        userService = Mockito.mock(UserServiceDefault.class);
        deviceRepository = Mockito.mock(DeviceRepository.class);
        deviceService = new DeviceServiceDefault(deviceRepository);
        eventPublisher = Mockito.mock(eventPublisher);
//        repairService = new RepairServiceDefault(repairRepository, userService,userRepository, deviceService, fileStorage, eventPublisher);

        testRepair = Repair.builder()
                .issuer(User
                        .builder()
                        .userInfo(new UserInfo())
                        .build())
                .device(new Device())
                .build();

        repairRepository.save(testRepair);
    }

//    @AfterEach
//    void cleanUp(){
//        repairRepository.delete(testRepair);
//    }

    @Test
    void findByIdShouldReturnARepair(){
        when(repairRepository.findById(testRepair.getId())).thenReturn(Optional.of(testRepair));
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
        List<Repair> toReturn = new ArrayList<>();
        toReturn.add(testRepair);

        when(repairRepository.findAll()).thenReturn(toReturn);
        List<Repair> expected = repairRepository.findAll();
        List<Repair> actual = repairService.findAllRepairs();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void findALlShouldThrowUserNotFoundExceptionWhenNoUsersAreFound(){
        List<Repair> allRepairs = repairRepository.findAll();

        Exception exception = assertThrows(RuntimeException.class, () ->{
            repairService.findAllRepairs();
        });

        String expectedMessage = "No repairs found!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
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
    public void updateShouldThrowNullPointerExceptionOnNullRepair(){
        Exception exception = assertThrows(NullPointerException.class, () ->{
            repairService.updateRepair(null);
        });
        String expectedMessage = "Repair can't be null!";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }


}
