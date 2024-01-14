package com.jakubwilk.serwisant.api.event.listeners;

import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import com.jakubwilk.serwisant.api.event.events.RepairStatusChangedEvent;
import com.jakubwilk.serwisant.api.service.EmailService;
import com.jakubwilk.serwisant.api.utils.ApplicationProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
@AllArgsConstructor
public class RepairStatusChangedEventListener implements ApplicationListener<RepairStatusChangedEvent> {
    private final EmailService emailService;

    @Override
    public void onApplicationEvent(RepairStatusChangedEvent event) {
        int repairId = event.getRepair().getId();
        Repair.RepairStatus repairStatus = event.getRepair().getRepairStatus();
        String frontUrl = new ApplicationProperties().getFrontUrl();
        String to = event.getUser().getEmail();
        String subject = "Zmienił się status naprawy #" + repairId;

        Context context = new Context();
        context.setVariable("repairId", repairId);
        context.setVariable("repairStatus", repairStatus);
        context.setVariable("repairLink", frontUrl);

        emailService.sendEmail(to, subject, "RepairStatusChangedEmail", context);
    }
}
