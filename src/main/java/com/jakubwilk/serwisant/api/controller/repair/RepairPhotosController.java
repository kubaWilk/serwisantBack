package com.jakubwilk.serwisant.api.controller.file;

import com.jakubwilk.serwisant.api.entity.jpa.File;
import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import com.jakubwilk.serwisant.api.service.FileService;
import com.jakubwilk.serwisant.api.service.RepairProtocolService;
import com.jakubwilk.serwisant.api.service.RepairService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/repair/{id}/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    private final RepairProtocolService repairProtocolService;
    private final RepairService repairService;

    @PostMapping("/single")
    public FileResponse uploadSingleFile(@RequestParam("file")MultipartFile file, @PathVariable("id") int repairId){
        File fileToSave;
        try {
            fileToSave = fileService.saveSingleFile(file, repairId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/repair/" + repairId + "/files/")
                .path(fileToSave.getId().toString())
                .toUriString();

        return new FileResponse(
                fileToSave.getFileName(),
                downloadURL,
                file.getContentType(),
                file.getSize());
    }

    @PostMapping("/multiple")
    public List<FileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,
                                                  @PathVariable("id") int repairId){
        List<FileResponse> savedFiles = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                File toSave = fileService.saveSingleFile(file, repairId);

                String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/download/")
                        .path(toSave.getId().toString())
                        .toUriString();
                FileResponse response = new FileResponse(toSave.getFileName(),
                        downloadUrl,
                        file.getContentType(),
                        file.getSize());
                savedFiles.add(response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return savedFiles;
    }

    @GetMapping("/all")
    public ResponseEntity<List<FileResponse>> getAllFiles(@PathVariable("id") int repairId) {
        List<File> files = fileService.getAllFiles(repairId);

        List<FileResponse> responseClasses = files.stream().map(product -> {
            String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/repair/" + repairId + "/files/")
                    .path(product.getId().toString())
                    .toUriString();
            return new FileResponse(product.getFileName(),
                    downloadURL,
                    product.getFileType(),
                    product.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.ok().body(responseClasses);
    }

    @GetMapping(
            value = "/{fileId}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getSingleFile(@PathVariable("fileId") UUID fileId) throws IOException {
        return fileService.getFileAsResource(fileId).getContentAsByteArray();
    }

    @GetMapping("/protocol/{repairId}")
    public ResponseEntity<Resource> getRepairCreatedProtocol(@PathVariable("repairId") int repairId){
        Repair repair = repairService.findById(repairId);
        java.io.File pdf =  repairProtocolService.getRepairCreatedProtocol(repair);

        Resource resource = new FileSystemResource(pdf);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdf.getName());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdf.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}