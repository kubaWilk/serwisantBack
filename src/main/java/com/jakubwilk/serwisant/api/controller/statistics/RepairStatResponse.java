package com.jakubwilk.serwisant.api.controller.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
public class RepairStatResponse {
    private LocalDate date;
    private String label;
    private long repairsOpened;
    private long repairsClosed;
}
