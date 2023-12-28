package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.entity.jpa.RepairDbFile;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface RepairDbFileService {
    RepairDbFile saveSingleFile(MultipartFile file, int repairId) throws IOException;

    List<RepairDbFile> saveFiles(MultipartFile[] files, int repairId) throws IOException;

    List<RepairDbFile> getAllFiles(int repairId);

    Resource getFileAsResource(UUID fileId);

    void deleteAll(int repairId);
}
