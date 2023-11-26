package com.jakubwilk.serwisant.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakubwilk.serwisant.api.entity.Repair;
import com.jakubwilk.serwisant.api.service.RepairService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repair")
public class RepairController {
    private final RepairService repairService;

    public RepairController(RepairService repairService) {
        this.repairService = repairService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Repair> getRepairById(@PathVariable("id") int id){
        Repair repair = repairService.findById(id);

        return new ResponseEntity<Repair>(repair, HttpStatus.OK);
    }

    @GetMapping("/")
    public List<Repair> getAllRepairs(){
        List<Repair> repairs = repairService.findAllRepairs();
        return repairs;
    }

    @PostMapping("/")
    public ResponseEntity<Repair> saveRepair(@RequestBody JsonNode repairJsonNode){
        Repair saved = repairService.saveRepair(repairJsonNode);

        return new ResponseEntity<Repair>(saved, HttpStatus.OK);
    }


    @PutMapping("/")
    public ResponseEntity<Repair> updateRepair(@RequestBody Repair repair){
        Repair updated = repairService.updateRepair(repair);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRepair(@PathVariable("id") int id){
        repairService.deleteRepair(id);
        return new ResponseEntity<>("Deleted repair: " + id, HttpStatus.OK);
    }
}
