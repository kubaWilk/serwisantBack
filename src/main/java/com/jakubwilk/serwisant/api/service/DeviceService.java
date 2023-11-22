package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.entity.Device;

import java.util.List;

public interface DeviceService {
    Device findById(int id);
    List<Device> findAllDevices();
    Device saveDevice(Device device);
    Device updateDevice(Device device);
    void deleteDevice(int id);
}
