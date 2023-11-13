package com.jakubwilk.serwisant.api.dao;

import com.jakubwilk.serwisant.api.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Integer> {
}
