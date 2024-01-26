package com.jakubwilk.serwisant.api.controller.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class CostStatResponse {
    private LocalDate date;
    private String label;
    private long costsPart;
    private long costsService;
}
