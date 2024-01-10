package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.repository.RepairDbFileRepository;
import com.jakubwilk.serwisant.api.entity.jpa.RepairDbFile;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class RepairDbFileServiceDefault implements RepairDbFileService {
    @Value("${spring.servlet.multipart.max-file-size}")
    private DataSize maxFileSize;
    private final RepairDbFileRepository repairDbFileRepository;

    public RepairDbFileServiceDefault(RepairDbFileRepository repairDbFileRepository) {
        this.repairDbFileRepository = repairDbFileRepository;
    }

    @Override
    @Transactional
    public RepairDbFile saveSingleFile(MultipartFile file, int repairId) throws IOException {
            if (file == null) throw new RuntimeException("File can't be null!");

            String filename = StringUtils.cleanPath(file.getOriginalFilename());

            if (file.getBytes().length > maxFileSize.toBytes()) {
                throw new RuntimeException("File size exceeds maximum limit!");
            }

            RepairDbFile fileToSave = new RepairDbFile(filename, file.getContentType(), repairId, file.getBytes());
            return repairDbFileRepository.save(fileToSave);
    }

    @Override
    @Transactional
    public List<RepairDbFile> saveFiles(MultipartFile[] files, int repairId) throws IOException {
        if(files == null) throw new RuntimeException("Files can't be null!");

        List<RepairDbFile> savedFiles = new ArrayList<>();
        Arrays.asList(files).forEach(file -> {
            try{
                savedFiles.add(saveSingleFile(file, repairId));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return savedFiles;
    }

    @Override
    @Transactional
    public List<RepairDbFile> getAllFiles(int repairId) {
        return repairDbFileRepository.findAllByRepairId(repairId);
    }

    @Override
    public Resource getFileAsResource(UUID fileId) {
        Optional<RepairDbFile> result = repairDbFileRepository.findById(fileId);

        if(result.isPresent()){
            RepairDbFile file = result.get();

            return new ByteArrayResource(file.getData());
        }else{
            throw new RuntimeException("No file with id of: " + fileId.toString());
        }
    }

    @Override
    @Transactional
    public void deleteAll(int repairId){
        repairDbFileRepository.deleteAllByRepairId(repairId);
    }
}
