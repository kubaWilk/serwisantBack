package com.jakubwilk.serwisant.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakubwilk.serwisant.api.dao.RepairRepository;
import com.jakubwilk.serwisant.api.dao.UserRepository;
import com.jakubwilk.serwisant.api.entity.Device;
import com.jakubwilk.serwisant.api.entity.Repair;
import com.jakubwilk.serwisant.api.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepairServiceDefault implements RepairService {
    private final RepairRepository repository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final DeviceService deviceService;

    private ApplicationEventPublisher eventPublisher;

    public RepairServiceDefault(RepairRepository repository, UserService userService, UserRepository userRepository, DeviceService deviceService, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.deviceService = deviceService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Repair findById(int id){
        Optional<Repair> result = repository.findById(id);

        if(result.isPresent()){
            return result.get();
        }else{
            throw new RuntimeException("No repair with id: " + id);
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
            throw new IllegalArgumentException("Repair must containt issuer's ID!");
        if(!repairJsonNode.has("device"))
            throw new IllegalArgumentException("Repair must contain device's ID!");

        //find issuer by id
        int issuerId = repairJsonNode.get("issuer").asInt();
        User issuer = userService.findById(issuerId);

        //find device by id
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
        if(checkForStatusUpdate(repair)){

        }

        if(repair == null) throw new NullPointerException(("Repair can't be null!"));
        return repository.save(repair);
    }

    @Override
    @Transactional
    public void deleteRepair(int id) {
        Optional <Repair> result = repository.findById(id);
        if(result.isPresent()){
            Repair repair = result.get();
            Device device = repair.getDevice();
            User issuer = repair.getIssuer();
            repair.setIssuer(null);
            repair.setDevice(null);
            device.removeRepair(repair);
            issuer.removeRepair(repair);
            repository.save(repair);
            userRepository.save(issuer);
            deviceService.saveDevice(device);
            repository.delete(repair);
        }else{
            throw new RuntimeException("No Repair found with id: " + id);
        }
    }

    public boolean checkForStatusUpdate(Repair repair){
        int theId = repair.getId();
        Optional<Repair> result = repository.findById(theId);

        if(result.isPresent()){
            return repair.getRepairStatus() != result.get().getRepairStatus();
        }else{
            throw new RuntimeException("No repair found with id: " + theId);
        }
    }
}