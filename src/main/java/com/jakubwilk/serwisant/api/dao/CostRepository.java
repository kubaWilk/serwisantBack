package com.jakubwilk.serwisant.api.dao;

import com.jakubwilk.serwisant.api.entity.Cost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CostRepository extends JpaRepository<Cost, Integer> {
}
