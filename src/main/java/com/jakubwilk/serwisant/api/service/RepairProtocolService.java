package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.entity.jpa.Device;
import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import com.jakubwilk.serwisant.api.entity.jpa.UserDetails;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.File;

@Service
public class RepairProtocolService {
    private final TemplateEngine templateEngine;
    private final FileService fileService;
    public RepairProtocolService(TemplateEngine templateEngine, FileService fileService){
        this.templateEngine = templateEngine;
        this.fileService = fileService;
    }

    public File getRepairCreatedProtocol(Repair repair){
        Context context = new Context();
        Device device = repair.getDevice();
        UserDetails userDetails = repair.getIssuer().getUserDetails();
        context.setVariable("manufacturer", device.getManufacturer());
        context.setVariable("model", device.getModel());
        context.setVariable("serialNumber", device.getSerialNumber());
        context.setVariable("firstName", userDetails.getFirstName());
        context.setVariable("lastName", userDetails.getLastName());
        context.setVariable("street", userDetails.getStreet());
        context.setVariable("postCode", userDetails.getPostCode());
        context.setVariable("city", userDetails.getCity());
        context.setVariable("description", repair.getDescription());
        context.setVariable("estimatedCost", repair.getEstimatedCost());


        String htmlProtocol = templateEngine.process("RepairCreatedProtocol", context);

        return fileService.getTempPdfFromAString(
                htmlProtocol,
                "RepairCreated",
                "_" + repair.getId());
    }

    public File getRepairClosedProtocol(Repair repair){
        return null;
    }
}
