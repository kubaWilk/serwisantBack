package com.jakubwilk.serwisant.api.controller.repair;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import com.jakubwilk.serwisant.api.service.RepairService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repair")
@AllArgsConstructor
@ControllerAdvice
public class RepairController {
    private final RepairService repairService;

    @GetMapping("/{id}")
    @PreAuthorize("ROLE_CUSTOMER")
    public ResponseEntity<Repair> getRepairById(@PathVariable("id") int id){
        Repair repair = repairService.findById(id);

        return new ResponseEntity<Repair>(repair, HttpStatus.OK);
    }

    @GetMapping("/")
    @Secured("ROLE_CUSTOMER")
    public List<Repair> getAllRepairs(Authentication authentication){
        System.out.println(authentication);
        List<Repair> repairs = repairService.findAllRepairs();
        return repairs;
    }

    @PostMapping("/")
    @Secured("ROLE_EMPLOYEE")
    public ResponseEntity<Repair> saveRepair(@RequestBody JsonNode repairJsonNode){
        Repair saved = repairService.saveRepair(repairJsonNode);

        return new ResponseEntity<Repair>(saved, HttpStatus.OK);
    }

    @PostMapping("/accept-cost/{id}")
    @Secured("ROLE_EMPLOYEE")
    public ResponseEntity acceptCost(@PathVariable("id") int repairId){
        return ResponseEntity.ok().build();
    }

    @PutMapping("/")
    @Secured("ROLE_EMPLOYEE")
    public ResponseEntity<Repair> updateRepair(@RequestBody Repair repair){
        Repair updated = repairService.updateRepair(repair);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> deleteRepair(@PathVariable("id") int id){
        repairService.deleteRepair(id);
        return new ResponseEntity<>("Deleted repair: " + id, HttpStatus.OK);
    }
}
