package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.entity.jpa.Device;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;

public interface DeviceService {
    Device findById(int id);
    List<Device> findAllDevices();
    Device saveDevice(Device device);

    Device handleDeviceUpdateByController(int id, Device device);

    @Transactional
    Device updateDevice(Device device);

    void deleteDevice(int id);
    Device searchDevice(Map<String,String> toSearch);
}
