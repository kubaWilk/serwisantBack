package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.entity.jpa.Device;
import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import com.jakubwilk.serwisant.api.entity.jpa.UserInfo;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.File;

@Service
public class RepairProtocolServiceDefault implements RepairProtocolService{
    private final TemplateEngine templateEngine;
    private final DocumentServiceDefault fileService;

    public RepairProtocolServiceDefault(TemplateEngine templateEngine, DocumentServiceDefault fileService){
        this.templateEngine = templateEngine;
        this.fileService = fileService;
    }

    @Override
    public File getRepairCreatedProtocol(Repair repair){
        Context context = new Context();
        Device device = repair.getDevice();
        UserInfo userInfo = repair.getIssuer().getUserInfo();
        context.setVariable("manufacturer", device.getManufacturer());
        context.setVariable("model", device.getModel());
        context.setVariable("serialNumber", device.getSerialNumber());
        context.setVariable("firstName", userInfo.getFirstName());
        context.setVariable("lastName", userInfo.getLastName());
        context.setVariable("street", userInfo.getStreet());
        context.setVariable("postCode", userInfo.getPostCode());
        context.setVariable("city", userInfo.getCity());
        context.setVariable("description", repair.getDescription());
        context.setVariable("estimatedCost", repair.getEstimatedCost());


        String htmlProtocol = templateEngine.process("RepairCreatedProtocol", context);

        return fileService.getTempPdfFromAString(
                htmlProtocol,
                "RepairCreated",
                "_" + repair.getId());
    }

    @Override
    public File getRepairClosedProtocol(Repair repair){
        return null;
    }
}
