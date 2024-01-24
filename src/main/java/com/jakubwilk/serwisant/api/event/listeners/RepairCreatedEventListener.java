package com.jakubwilk.serwisant.api.event.listeners;

import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import com.jakubwilk.serwisant.api.event.events.RepairCreatedEvent;
import com.jakubwilk.serwisant.api.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
@AllArgsConstructor
public class RepairCreatedEventListener implements ApplicationListener<RepairCreatedEvent> {
    private final EmailService emailService;
    @Override
    public void onApplicationEvent(RepairCreatedEvent event) {
        String to = event.getUser().getEmail();
        Repair repair = event.getRepair();
        String subject = "Utworzono naprawę #" + repair.getId();

        Context context = new Context();
        context.setVariable("manufacturer", repair.getDevice().getManufacturer());
        context.setVariable("model", repair.getDevice().getModel());
        context.setVariable("serialNumber", repair.getDevice().getSerialNumber());

        emailService.sendEmail(to, subject, "RepairCreatedEmail", context);
    }
}
