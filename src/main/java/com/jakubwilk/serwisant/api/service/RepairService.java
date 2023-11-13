package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.dao.RepairRepository;
import com.jakubwilk.serwisant.api.entity.Repair;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RepairService {
    private RepairRepository repository;

    public RepairService(RepairRepository repository) {
        this.repository = repository;
    }

    public Repair findById(int id){
        Optional<Repair> result = repository.findById(id);

        if(result.isPresent()){
            return result.get();
        }else{
            throw new IllegalArgumentException("No repair with id: " + id);
        }
    }
}
