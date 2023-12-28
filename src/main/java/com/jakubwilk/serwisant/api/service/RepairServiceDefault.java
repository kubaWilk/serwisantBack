package com.jakubwilk.serwisant.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakubwilk.serwisant.api.repository.RepairRepository;
import com.jakubwilk.serwisant.api.repository.UserRepository;
import com.jakubwilk.serwisant.api.entity.jpa.Device;
import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import com.jakubwilk.serwisant.api.entity.jpa.User;
import com.jakubwilk.serwisant.api.event.events.RepairCreatedEvent;
import com.jakubwilk.serwisant.api.event.events.RepairStatusChangedEvent;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RepairServiceDefault implements RepairService {
    private final RepairRepository repository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final DeviceService deviceService;
    private final RepairDbFileService fileService;
    private ApplicationEventPublisher eventPublisher;

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

        String description = repairJsonNode.get("description").asText("");
        double estimatedCost = repairJsonNode.get("estimatedCost").asDouble(0);

        Repair newRepair = Repair.builder()
                .issuer(issuer)
                .device(device)
                .repairStatus(Repair.Status.OPEN)
                .description(description)
                .estimatedCost(estimatedCost)
                .build();

        eventPublisher.publishEvent(new RepairCreatedEvent(RepairServiceDefault.class, issuer, newRepair));
        return repository.save(newRepair);
    }

    @Override
    @Transactional
    public Repair updateRepair(Repair repair) {
        if(repair == null) throw new NullPointerException(("Repair can't be null!"));
        Repair updated;

        if(checkForStatusUpdate(repair)){
            updated = repository.save(repair);
            eventPublisher.publishEvent(
                    new RepairStatusChangedEvent(RepairServiceDefault.class, repair.getIssuer(), updated));
        }else{
            updated = repository.save(repair);
        }

        return updated;
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
            fileService.deleteAll(id);
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