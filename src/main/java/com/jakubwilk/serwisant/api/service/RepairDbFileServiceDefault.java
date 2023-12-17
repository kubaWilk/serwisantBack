package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.dao.FileRepository;
import com.jakubwilk.serwisant.api.entity.jpa.InDbFile;
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
public class FileServiceDefault implements FileService{
    @Value("${spring.servlet.multipart.max-file-size}")
    private DataSize maxFileSize;
    private final FileRepository fileRepository;

    public FileServiceDefault(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    @Transactional
    public InDbFile saveSingleFile(MultipartFile file, int repairId) throws IOException {
            if (file == null) throw new RuntimeException("File can't be null!");

            String filename = StringUtils.cleanPath(file.getOriginalFilename());

            if (file.getBytes().length > maxFileSize.toBytes()) {
                throw new RuntimeException("File size exceeds maximum limit!");
            }

            InDbFile fileToSave = new InDbFile(filename, file.getContentType(), repairId, file.getBytes());
            return fileRepository.save(fileToSave);
    }

    @Override
    @Transactional
    public List<InDbFile> saveFiles(MultipartFile[] files, int repairId) throws IOException {
        if(files == null) throw new RuntimeException("Files can't be null!");

        List<InDbFile> savedFiles = new ArrayList<>();
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
    public List<InDbFile> getAllFiles(int repairId) {
        return fileRepository.findAllByRepairId(repairId);
    }

    @Override
    public Resource getFileAsResource(UUID fileId) {
        Optional<InDbFile> result = fileRepository.findById(fileId);

        if(result.isPresent()){
            InDbFile file = result.get();

            return new ByteArrayResource(file.getData());
        }else{
            throw new RuntimeException("No file with id of: " + fileId.toString());
        }
    }

    @Override
    @Transactional
    public void deleteAll(int repairId){
        fileRepository.deleteAllByRepairId(repairId);
    }
}
