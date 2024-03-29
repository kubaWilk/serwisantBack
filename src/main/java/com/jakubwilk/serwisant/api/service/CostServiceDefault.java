package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import com.jakubwilk.serwisant.api.exception.RepairNotFoundException;
import com.jakubwilk.serwisant.api.repository.CostRepository;
import com.jakubwilk.serwisant.api.entity.jpa.Cost;
import com.jakubwilk.serwisant.api.repository.RepairRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CostServiceDefault implements CostService{
    private final CostRepository costRepository;
    private final RepairRepository repairRepository;

    public CostServiceDefault(CostRepository costRepository, RepairRepository repairRepository) {
        this.costRepository = costRepository;
        this.repairRepository = repairRepository;
    }

    @Override
    public Cost findById(int id) {
        Optional<Cost> result = costRepository.findById(id);

        if(result.isPresent()){
            return result.get();
        }else{
            throw new RuntimeException("Cost with id: " + id + " not found");
        }
    }

    @Override
    public List<Cost> findAll() {
        List<Cost> result = costRepository.findAll();

        if(result.isEmpty()) throw new RuntimeException("No costs were found.");
        return result;
    }

    @Override
    @Transactional
    public Cost saveCost(Cost cost) {
        return costRepository.save(cost);
    }

    @Override
    @Transactional
    public Cost updateCost(Cost cost) {
        if(cost.getId() == 0) throw new IllegalArgumentException("Cost to update has to be provided with id!");
        return costRepository.save(cost);
    }

    @Override
    @Transactional
    public void deleteCost(int id) {
        costRepository.deleteById(id);
    }

    @Override
    public List<Cost> findAllCostsByRepairId(int repairId) {
        return costRepository.findAllByRepairId(repairId);
    }

    @Override
    @Transactional
    public Cost saveCost(Cost cost, int repairId) {
        Optional<Repair> result = repairRepository.findById(repairId);
        if(result.isEmpty()) {
            throw new RepairNotFoundException("Couldn't find repair with id: " + repairId);
        }
        Repair repair = result.get();
        if(repair.isCostAccepted()) repair.setCostAccepted(false);
        repairRepository.save(repair);

        cost.setRepair(repair);
        repair.addCost(cost);
        return costRepository.save(cost);
    }
}
