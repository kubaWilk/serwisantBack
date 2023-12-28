package com.jakubwilk.serwisant.api.controller;

import com.jakubwilk.serwisant.api.entity.jpa.Cost;
import com.jakubwilk.serwisant.api.service.CostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cost")
public class CostController {
    private final CostService costService;

    public CostController(CostService costService) {
        this.costService = costService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cost> findCostById(@PathVariable("id") int id){
        Cost found = costService.findById(id);
        return ResponseEntity.ok(found);
    }

    @GetMapping
    public ResponseEntity<List<Cost>> getAllCosts(){
        List<Cost> found = costService.findAll();
        return ResponseEntity.ok(found);
    }

    @PostMapping
    public ResponseEntity<Cost> saveCost(@RequestBody Cost cost){
        Cost saved = costService.saveCost(cost);
        return ResponseEntity.ok(saved);
    }

    @PutMapping
    public ResponseEntity<Cost> updateCost(@RequestBody Cost cost){
        Cost updated = costService.updateCost(cost);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCost(@PathVariable("id") int id){
        costService.deleteCost(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
