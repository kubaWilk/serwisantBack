package com.jakubwilk.serwisant.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakubwilk.serwisant.api.entity.jpa.*;
import com.jakubwilk.serwisant.api.exception.RepairNotFoundException;
import com.jakubwilk.serwisant.api.repository.RepairRepository;
import com.jakubwilk.serwisant.api.repository.UserRepository;
import com.jakubwilk.serwisant.api.event.events.RepairCreatedEvent;
import com.jakubwilk.serwisant.api.event.events.RepairStatusChangedEvent;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

        if(repairs.isEmpty()) throw new RepairNotFoundException("No repairs found!");
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
                .repairStatus(RepairStatus.OPEN)
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
    public Repair updateRepairStatus(Map<String, String> toUpdate) {
        if(!toUpdate.containsKey("id")) throw new IllegalArgumentException("To update a repair status you must provide a repair id!");
        if(!toUpdate.containsKey("status")) throw new IllegalArgumentException("To update a repair you must provide a valid status!");

        int repairId;
        RepairStatus status;

        try {
            repairId = Integer.parseInt(toUpdate.get("id"));
            status = RepairStatus.valueOf(toUpdate.get("status"));
        }catch(NumberFormatException exception){
            throw new IllegalArgumentException("ID must be a valid repair number!", exception);
        }catch(IllegalArgumentException exception){
            throw new IllegalArgumentException("Must provide a valid repair status!", exception);
        }

        Repair toUpdateStatus = findById(repairId);
        toUpdateStatus.setRepairStatus(status);
        toUpdateStatus.setStatusModifiedAt(LocalDateTime.now());
        Repair updated = repository.save(toUpdateStatus);
        eventPublisher.publishEvent(
                new RepairStatusChangedEvent(RepairServiceDefault.class, updated.getIssuer(), updated));

        return updated;
    }

    @Override
    @Transactional
    public void deleteCostFromARepair(int repairId, Cost theCost){
        Optional<Repair> repair = repository.findById(repairId);
        if(repair.isEmpty()) throw new RuntimeException("Couldn't find repair with id of: " + repairId);
        repair.get().removeCost(theCost);

        repository.save(repair.get());
    }

    @Override
    @Transactional
    public void deleteRepair(int id) {
        Optional <Repair> result = repository.findById(id);

        if(result.isPresent()){
            Repair repair = result.get();
            fileService.deleteAll(id);
            repository.delete(repair);
        }else{
            throw new RuntimeException("No Repair found with id: " + id);
        }
    }

    @Override
    public List<Repair> findAllRepairsByCustomerId(int id) {
        return repository.findAllByIssuerId(id);
    }

    @Override
    public List<Repair> findAllRepairsByDeviceId(int deviceId) {
        return repository.findAllByDeviceId(deviceId);
    }

    private boolean checkForStatusUpdate(Repair repair){
        int theId = repair.getId();
        Optional<Repair> result = repository.findById(theId);

        if(result.isPresent()){
            return repair.getRepairStatus() != result.get().getRepairStatus();
        }else{
            throw new RuntimeException("No repair found with id: " + theId);
        }
    }
}