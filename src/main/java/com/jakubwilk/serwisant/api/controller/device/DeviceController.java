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
import java.util.Map;

@RestController
@RequestMapping("/device")
@ControllerAdvice
public class DeviceController {
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/{id}")
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
    @Secured("ROLE_EMPLOYEE")
    public ResponseEntity<Device> saveDevice(@RequestBody Device device){
        Device saved = deviceService.saveDevice(device);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/search")
    @Secured("ROLE_EMPLOYEE")
    public ResponseEntity<Device> searchDevice(@RequestBody Map<String,String> toSearch){
        Device device = deviceService.searchDevice(toSearch);
        return ResponseEntity.ok(device);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_EMPLOYEE")
    public ResponseEntity<Device> updateDevice(@PathVariable int id, @RequestBody Device device){
        Device updated = deviceService.updateDevice(id, device);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<HttpStatus> deleteDevice(@PathVariable("id") int id){
        deviceService.deleteDevice(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
