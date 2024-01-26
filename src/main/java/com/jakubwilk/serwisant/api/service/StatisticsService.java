package com.jakubwilk.serwisant.api.service;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface StatisticsService<T> {
    List<T> getDaily(LocalDate from, LocalDate to);
    List<T> getWeekly(LocalDate from, LocalDate to);
    List<T> getMonthly(LocalDate from, LocalDate to);
    List<T> getYearly(LocalDate from, LocalDate to);
}
