package com.jakubwilk.serwisant.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakubwilk.serwisant.api.dao.RepairRepository;
import com.jakubwilk.serwisant.api.entity.Device;
import com.jakubwilk.serwisant.api.entity.Repair;
import com.jakubwilk.serwisant.api.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepairServiceDefault implements RepairService {
    private final RepairRepository repository;
    private final UserService userService;
    private final DeviceService deviceService;

    public RepairServiceDefault(RepairRepository repository, UserService userService, DeviceService deviceService) {
        this.repository = repository;
        this.userService = userService;
        this.deviceService = deviceService;
    }

    @Override
    public Repair findById(int id){
        Optional<Repair> result = repository.findById(id);

        if(result.isPresent()){
            return result.get();
        }else{
            throw new IllegalArgumentException("No repair with id: " + id);
        }
    }

    @Override
    public List<Repair> findAllRepairs() {
        List<Repair> repairs = repository.findAll();

        if(repairs.isEmpty()) throw new RuntimeException("No repairs found!");
        return repairs;
    }

    @Override
    @Transactional
    public Repair saveRepair(JsonNode repairJsonNode) {
        if(repairJsonNode == null) throw new NullPointerException(("Repair can't be null!"));
        if(!repairJsonNode.has("issuer"))
            throw new IllegalArgumentException("Note must containt issuer's ID!");
        //find issuer by id
        int issuerId = repairJsonNode.get("issuer").asInt();
        User issuer = userService.findById(issuerId);

        //find device by id
        if(!repairJsonNode.has("device"))
            throw new IllegalArgumentException("Repair must contain device's ID!");
        int deviceId = repairJsonNode.get("device").asInt();
        Device device = deviceService.findById(deviceId);

        Repair newRepair = Repair.builder()
                .issuer(issuer)
                .device(device)
                .repairStatus(Repair.Status.OPEN)
                .build();

        return repository.save(newRepair);
    }

    @Override
    @Transactional
    public Repair updateRepair(Repair repair) {
        return repository.save(repair);
    }

    @Override
    @Transactional
    public void deleteRepair(int id) {
        repository.deleteById(id);
    }
}
