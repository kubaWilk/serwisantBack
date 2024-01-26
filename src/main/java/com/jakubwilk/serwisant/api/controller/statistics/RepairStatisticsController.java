package com.jakubwilk.serwisant.api.controller.statistics;

import com.jakubwilk.serwisant.api.service.StatisticsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@ControllerAdvice
@RequestMapping("/statistics/repair")
public class RepairStatisticsController {
    private final StatisticsService<RepairStatResponse> statisticsService;

    public RepairStatisticsController(@Qualifier("repairStatisticsService") StatisticsService<RepairStatResponse> statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/daily")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<RepairStatResponse>> getRepairStatsDaily(
                                                            @RequestParam("from") LocalDate from,
                                                            @RequestParam("to") LocalDate to){
        return ResponseEntity.ok(statisticsService.getDaily(from, to));
    }

    @GetMapping("/weekly")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<RepairStatResponse>> getRepairStatsWeekly(@RequestParam("from") LocalDate from,
                                                                         @RequestParam("to") LocalDate to){
        return ResponseEntity.ok(statisticsService.getWeekly(from,to));
    }

    @GetMapping("/monthly")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<RepairStatResponse>> getRepairStatsMonthly(@RequestParam("from") LocalDate from,
                                                                          @RequestParam("to") LocalDate to){
        return ResponseEntity.ok(statisticsService.getMonthly(from,to));
    }

    @GetMapping("/yearly")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<RepairStatResponse>> getRepairStatsYearly(@RequestParam("from") LocalDate from,
                                                                         @RequestParam("to") LocalDate to){
        return ResponseEntity.ok(statisticsService.getYearly(from,to));
    }
}
