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
@RequestMapping("/statistics/cost")
public class CostStatisticsController {
    private final StatisticsService<CostStatResponse> statisticsService;

    public CostStatisticsController(
            @Qualifier("costStatisticsService")StatisticsService<CostStatResponse> statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/daily")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<CostStatResponse>> getCostStatsDaily(@RequestParam("from") LocalDate from,
                                                                    @RequestParam("to") LocalDate to){
        return ResponseEntity.ok(statisticsService.getDaily(from, to));
    }

    @GetMapping("/weekly")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<CostStatResponse>> getCostStatsWeekly(@RequestParam("from") LocalDate from,
                                                                    @RequestParam("to") LocalDate to){
        return ResponseEntity.ok(statisticsService.getWeekly(from, to));
    }

    @GetMapping("/monthly")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<CostStatResponse>> getCostStatsMonthly(@RequestParam("from") LocalDate from,
                                                                     @RequestParam("to") LocalDate to){
        return ResponseEntity.ok(statisticsService.getMonthly(from, to));
    }

    @GetMapping("/yearly")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<CostStatResponse>> getCostStatsYearly(@RequestParam("from") LocalDate from,
                                                                     @RequestParam("to") LocalDate to){
        return ResponseEntity.ok(statisticsService.getYearly(from, to));
    }
}
