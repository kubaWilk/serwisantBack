package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.repository.DeviceRepository;
import com.jakubwilk.serwisant.api.entity.jpa.Device;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
public class DeviceServiceTests {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceServiceDefault deviceService;
    private Device testDevice;

    @BeforeEach
    public void setupTestData(){
        testDevice = Device.builder()
                .manufacturer("testManufacturer")
                .model("testModel")
                .serialNumber("testSerialNumber")
                .build();
        deviceRepository.save(testDevice);
    }

    @AfterEach
    public void cleanUp(){
        deviceRepository.delete(testDevice);
    }

    @Test
    public void findByIdShouldReturnDevice(){
        Device expected = deviceRepository.findById(testDevice.getId()).get();
        Device actual = deviceService.findById(testDevice.getId());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void findByIdShouldThrowRuntimeExceptionOnNoDevice(){
        int theId = testDevice.getId();
        deviceRepository.delete(testDevice);

        Exception exception = assertThrows(RuntimeException.class, () ->{
            deviceService.findById(theId);
        });
        String expectedMessage = "Couldn't find device with id: " + theId;
        String actualMesage = exception.getMessage();

        assertEquals(expectedMessage, actualMesage);
    }

    @Test
    public void findAllDevicesShouldFindAllDevices(){
        List<Device> expected = deviceRepository.findAll();
        List<Device> actual = deviceService.findAllDevices();

        assertThat(expected).usingRecursiveComparison().isEqualTo(actual);
    }

    @Test
    public void findAllDevicesShouldThrowRuntimeExceptionOnNoDevicesFound(){
        deviceRepository.deleteAll();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            deviceService.findAllDevices();
        });
        String expectedMessage = "No Devices found!";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void saveShouldSaveTheDevice(){
        Device expected = Device.builder()
                .manufacturer("anotherTestManufacturer")
                .model("anotherTestModel")
                .serialNumber("anotherTestSerialNumber")
                .build();

        Device actual = deviceService.saveDevice(expected);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    public void saveShouldThrowNullPointerExceptionOnNullDevicePassed(){
        Exception exception = assertThrows(NullPointerException.class, () ->{
            deviceService.saveDevice(null);
        });

        String expectedMessage = "Device can't be null!";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void updateShouldUpdateDevice(){
        testDevice.setManufacturer("changedManufacturer");
//        Device actual = deviceService.updateDevice(testDevice);

//        assertThat(actual).usingRecursiveComparison().isEqualTo(testDevice);
    }

    @Test
    public void updateShouldThrowNullPointerExceptionOnNullDevicePassed(){
//        Exception exception = assertThrows(NullPointerException.class, () ->{
//            deviceService.updateDevice(null);
//        });

        String expectedMessage = "Device can't be null!";
//        String actualMessage = exception.getMessage();

//        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void deleteShouldDeleteTheDevice(){
        int theId = testDevice.getId();
        deviceService.deleteDevice(theId);

        Optional<Device> actual =  deviceRepository.findById(theId);
        assertFalse(actual.isPresent());
    }
}
