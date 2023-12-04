package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.dao.DeviceRepository;
import com.jakubwilk.serwisant.api.entity.Device;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceServiceDefault implements DeviceService{
    private final DeviceRepository repository;

    public DeviceServiceDefault(DeviceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Device findById(int id) {
        Optional<Device> result = repository.findById(id);
        if(result.isPresent()){
            return result.get();
        }else{
            throw new RuntimeException("Couldn't find device with id: " + id);
        }
    }

    @Override
    public List<Device> findAllDevices() {
        List<Device> found = repository.findAll();

        if(found.isEmpty()) throw new RuntimeException("No Devices found!");
        return found;
    }

    @Override
    public Device saveDevice(Device device) {
        if(device == null) throw new NullPointerException("Device can't be null!");
        return repository.save(device);
    }

    @Override
    public Device updateDevice(Device device) {
        if(device == null) throw new NullPointerException("Device can't be null!");
        return repository.save(device);
    }

    @Override
    public void deleteDevice(int id) {
        repository.deleteById(id);
    }
}
