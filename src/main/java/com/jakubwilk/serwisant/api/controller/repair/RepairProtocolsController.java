package com.jakubwilk.serwisant.api.controller.repair;

import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import com.jakubwilk.serwisant.api.service.RepairProtocolService;
import com.jakubwilk.serwisant.api.service.RepairService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/repair/{repairId}/protocols")
@AllArgsConstructor
public class RepairProtocolsController {

    private final RepairService repairService;
    private final RepairProtocolService repairProtocolService;
    @GetMapping("/create")
    public ResponseEntity<Resource> getRepairCreatedProtocol(@PathVariable("repairId") int repairId){
        Repair repair = repairService.findById(repairId);
        File pdf = repairProtocolService.getRepairCreatedProtocol(repair);
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
