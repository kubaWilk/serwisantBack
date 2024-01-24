package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import com.jakubwilk.serwisant.api.exception.DeviceNotFoundException;
import com.jakubwilk.serwisant.api.repository.DeviceRepository;
import com.jakubwilk.serwisant.api.entity.jpa.Device;
import com.jakubwilk.serwisant.api.repository.RepairRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Secured("ROLE_CUSTOMER")
public class DeviceServiceDefault implements DeviceService{
    private final DeviceRepository repository;
    private final RepairRepository repairRepository;

    public DeviceServiceDefault(DeviceRepository repository, RepairRepository repairRepository) {
        this.repository = repository;
        this.repairRepository = repairRepository;
    }

    @Override
    public Device findById(int id) {
        Optional<Device> result = repository.findById(id);
        if(result.isPresent()){
            return result.get();
        }else{
            throw new DeviceNotFoundException("Couldn't find device with id: " + id);
        }
    }

    @Override
    public List<Device> findAllDevices() {
        List<Device> found = repository.findAll();

        if(found.isEmpty()) throw new DeviceNotFoundException("No Devices found!");
        return found;
    }

    @Override
    public Device saveDevice(Device device) {
        if(device == null) throw new NullPointerException("Device can't be null!");
        if(doesDeviceWithGivenSerialNumberExist(device))
            throw new IllegalArgumentException("Device with given serial number already exists!");
        return repository.save(device);
    }

    @Override
    @Transactional
    public Device handleDeviceUpdateByController(int id, Device device) {
        if(device == null) throw new NullPointerException("Device can't be null!");
        Optional<Device> result = repository.findById(id);
        if(result.isPresent()){
            Device toUpdate = result.get();
            toUpdate.setManufacturer(device.getManufacturer());
            toUpdate.setSerialNumber(device.getSerialNumber());
            toUpdate.setModel(device.getModel());
            return repository.save(device);
        }else{
            throw new DeviceNotFoundException("Can't find device with id: " + id);
        }
    }

    @Override
    @Transactional
    public Device updateDevice(Device device){
        if(device == null) throw new NullPointerException("Device can't be null!");
        return repository.saveAndFlush(device);
    }

    @Override
    @Transactional
    public void deleteDevice(int id) {
        repository.deleteById(id);
    }

//    Optional<Device> result = repository.findById(id);
//        if(result.isEmpty()) throw new DeviceNotFoundException("No device with id of: " + id +" has been found!");
//
//        Device device = result.get();
//        for(Repair repair: device.getRepairs()){
//            repairRepository.delete(repair);
//        }


    @Override
    public Device searchDevice(Map<String,String> toSearch) {
        if (toSearch.containsKey("serialNumber")) {
            Optional<Device> result = repository.findBySerialNumber(toSearch.get("serialNumber"));

            if (result.isPresent()) {
                return result.get();
            }
        }
        throw new DeviceNotFoundException("No device found with given serialNumber!");
    }

    private boolean doesDeviceWithGivenSerialNumberExist(Device device){
        return repository.findBySerialNumber(device.getSerialNumber()).isPresent();
    }
}
