package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.entity.jpa.InDbFile;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FileService {
    InDbFile saveSingleFile(MultipartFile file, int repairId) throws IOException;

    List<InDbFile> saveFiles(MultipartFile[] files, int repairId) throws IOException;

    List<InDbFile> getAllFiles(int repairId);

    Resource getFileAsResource(UUID fileId);

    void deleteAll(int repairId);
}
