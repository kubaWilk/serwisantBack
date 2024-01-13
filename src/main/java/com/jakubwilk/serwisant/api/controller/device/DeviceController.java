package com.jakubwilk.serwisant.api.controller.device;

import com.jakubwilk.serwisant.api.entity.jpa.Device;
import com.jakubwilk.serwisant.api.service.DeviceService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/device")
@ControllerAdvice
public class DeviceController {
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("{id}")
    @Secured("ROLE_CUSTOMER")
    public ResponseEntity<Device> findDeviceById(@PathVariable("id") int id){
        Device found = deviceService.findById(id);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/")
    @Secured("ROLE_CUSTOMER")
    public ResponseEntity<List<Device>> findAllDevices(){
        List<Device> found = deviceService.findAllDevices();
        return ResponseEntity.ok(found);
    }

    @PostMapping("/")
    @Secured("ROLE_USER")
    public ResponseEntity<Device> saveDevice(@RequestBody Device device){
        Device saved = deviceService.saveDevice(device);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/")
    @Secured("ROLE_USER")
    public ResponseEntity<Device> updateDevice(@RequestBody Device device){
        Device updated = deviceService.updateDevice(device);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<HttpStatus> deleteDevice(@PathVariable("id") int id){
        deviceService.deleteDevice(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
