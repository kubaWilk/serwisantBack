package com.jakubwilk.serwisant.api.controller.repair;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakubwilk.serwisant.api.entity.ProtocolType;
import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import com.jakubwilk.serwisant.api.entity.jpa.RepairDbFile;
import com.jakubwilk.serwisant.api.entity.jpa.RepairStatus;
import com.jakubwilk.serwisant.api.service.RepairDbFileService;
import com.jakubwilk.serwisant.api.service.RepairProtocolService;
import com.jakubwilk.serwisant.api.service.RepairService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import static com.jakubwilk.serwisant.api.entity.jpa.User.isOnlyCustomer;

@RestController
@RequestMapping("/repair")
@AllArgsConstructor
@ControllerAdvice
public class RepairController {
    private final RepairService repairService;
    private final RepairProtocolService repairProtocolService;
    private final RepairDbFileService fileService;



    @GetMapping("/{id}")
    @Secured("ROLE_CUSTOMER")
    public ResponseEntity<Repair> getRepairById(@PathVariable("id") int id, JwtAuthenticationToken authenticationToken){
        if(isOnlyCustomer(authenticationToken))
            return new ResponseEntity<>(repairService.findById(id, (Principal) authenticationToken.getPrincipal()),
                    HttpStatus.OK);
        else {
            return new ResponseEntity<Repair>(repairService.findById(id), HttpStatus.OK);
        }
    }

    @GetMapping("/")
    @Secured("ROLE_CUSTOMER")
    public List<Repair> getAllRepairs(JwtAuthenticationToken authentication){
        if(isOnlyCustomer(authentication)) {
            return repairService.findAllCustomerRepairs(authentication);
        }
        else {
            return repairService.findAllRepairs();
        }
    }

    @GetMapping("/customer/{customerId}")
    @Secured("ROLE_EMPLOYEE")
    public ResponseEntity<List<Repair>> getAllRepairsByCustomerId(@PathVariable("customerId") int customerId){
        List<Repair> result = repairService.findAllRepairsByCustomerId(customerId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/device/{deviceId}")
    @Secured("ROLE_EMPLOYEE")
    public ResponseEntity<List<Repair>> getAllRepairsByDeviceId(@PathVariable("deviceId") int deviceId){
        List<Repair> result = repairService.findAllRepairsByDeviceId(deviceId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{repairId}/protocol")
    @Secured("ROLE_EMPLOYEE")
    public ResponseEntity<Resource> getARepairProtocol(@PathVariable("repairId") int repairId, @RequestParam("protocolType")ProtocolType protocolType){
        File result = repairProtocolService.getRepairProtocol(repairId, protocolType);
        Resource resource = new FileSystemResource(result);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + result.getName()+".pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @PostMapping("/")
    @Secured("ROLE_EMPLOYEE")
    public ResponseEntity<Repair> saveRepair(@RequestBody JsonNode repairJsonNode){
        Repair saved = repairService.saveRepair(repairJsonNode);

        return new ResponseEntity<Repair>(saved, HttpStatus.OK);
    }

    @PostMapping(path = "/{repairId}/photos", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Secured("ROLE_EMPLOYEE")
    public ResponseEntity<List<RepairDbFile>> test(@PathVariable int repairId, @RequestParam("files") MultipartFile[] files) throws IOException {
        return ResponseEntity.ok(fileService.saveFiles(files,repairId));
    }

    @PostMapping("/accept-cost/{id}")
    @Secured("ROLE_CUSTOMER")
    public ResponseEntity<Object> acceptCost(@PathVariable("id") int repairId){
        repairService.acceptCosts(repairId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/")
    @Secured("ROLE_EMPLOYEE")
    public ResponseEntity<Repair> updateRepair(@RequestBody Repair repair){
        Repair updated = repairService.updateRepair(repair);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PutMapping("/status")
    @Secured("ROLE_EMPLOYEE")
    public ResponseEntity<Repair> updateRepairStatus(@RequestBody Map<String, String> toUpdate){
        return ResponseEntity.ok(repairService.updateRepairStatus(toUpdate));
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> deleteRepair(@PathVariable("id") int id){
        repairService.deleteRepair(id);
        return new ResponseEntity<>("Deleted repair: " + id, HttpStatus.OK);
    }


}