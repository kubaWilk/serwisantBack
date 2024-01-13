package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.entity.jpa.Repair;

import java.io.File;

public interface RepairProtocolService {
    File getRepairCreatedProtocol(Repair repair);

    File getRepairClosedProtocol(Repair repair);
}
