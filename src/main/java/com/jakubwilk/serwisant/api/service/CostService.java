package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.entity.jpa.Cost;

import java.util.List;

public interface CostService {
    Cost findById(int id);
    List<Cost> findAll();
    Cost saveCost(Cost cost);
    Cost updateCost(Cost cost);
    void deleteCost(int id);
}
