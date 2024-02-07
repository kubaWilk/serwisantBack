package com.jakubwilk.serwisant.api.controller.cost;

import com.jakubwilk.serwisant.api.entity.jpa.Cost;
import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import com.jakubwilk.serwisant.api.service.CostService;
import com.jakubwilk.serwisant.api.service.RepairService;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cost")
@ControllerAdvice
public class CostController {
    private final CostService costService;
    private final RepairService repairService;

    public CostController(CostService costService, RepairService repairService) {
        this.costService = costService;
        this.repairService = repairService;
    }

    @GetMapping("/{id}")
    @Secured("ROLE_CUSTOMER")
    public ResponseEntity<Cost> findCostById(@PathVariable("id") int id){
        Cost found = costService.findById(id);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<Cost>> getAllCosts(){
        List<Cost> found = costService.findAll();
        return ResponseEntity.ok(found);
    }

    @GetMapping("/repair")
    @Secured("ROLE_CUSTOMER")
    public ResponseEntity<List<Cost>> getAllCostsByRepairId(@RequestParam("id") int repairId){
        List <Cost> result = costService.findAllCostsByRepairId(repairId);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/")
    @Secured("ROLE_EMPLOYEE")
    public ResponseEntity<Cost> saveCost(@RequestBody Cost cost, @RequestParam("repairid") int repairId){
        Cost saved = costService.saveCost(cost, repairId);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/")
    @Secured("ROLE_EMPLOYEE")
    public ResponseEntity<Cost> updateCost(@RequestBody Cost cost){
        Cost updated = costService.updateCost(cost);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_EMPLOYEE")
    public ResponseEntity<HttpStatus> deleteCost(@PathVariable("id") int id){
        costService.deleteCost(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
