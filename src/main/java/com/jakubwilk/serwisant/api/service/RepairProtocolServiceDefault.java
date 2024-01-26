package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.entity.ProtocolType;
import com.jakubwilk.serwisant.api.entity.jpa.Device;
import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import com.jakubwilk.serwisant.api.entity.jpa.UserInfo;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.File;

@Service
@Secured("ROLE_CUSTOMER")
public class RepairProtocolServiceDefault implements RepairProtocolService{
    private final TemplateEngine templateEngine;
    private final DocumentServiceDefault fileService;
    private final RepairService repairService;

    public RepairProtocolServiceDefault(TemplateEngine templateEngine, DocumentServiceDefault fileService, RepairService repairService){
        this.templateEngine = templateEngine;
        this.fileService = fileService;
        this.repairService = repairService;
    }

    @Override
    public File getRepairProtocol(int id, ProtocolType protocolType){
        Repair repair = repairService.findById(id, principal);
        switch(protocolType){
            case REPAIR_OPENED -> {
                return getRepairCreatedProtocol(repair);
            }
            case REPAIR_CLOSED -> {
                return getRepairClosedProtocol(repair);
            }
        }
        throw new RuntimeException("Couldn't create requested protocol");
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
