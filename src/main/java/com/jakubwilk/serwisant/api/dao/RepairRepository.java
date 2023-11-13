package com.jakubwilk.serwisant.api.dao;

import com.jakubwilk.serwisant.api.entity.Repair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepairRepository extends JpaRepository<Repair, Integer> {
}
