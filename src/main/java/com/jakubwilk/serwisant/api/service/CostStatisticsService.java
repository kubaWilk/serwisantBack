package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.controller.statistics.CostStatResponse;
import com.jakubwilk.serwisant.api.controller.statistics.RepairStatResponse;
import com.jakubwilk.serwisant.api.entity.jpa.Cost;
import com.jakubwilk.serwisant.api.repository.CostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class CostStatisticsService implements StatisticsService<CostStatResponse>{
    private final CostRepository costRepository;
    @Override
    public List<CostStatResponse> getDaily(LocalDate from, LocalDate to) {
        List<Cost> costs = costRepository.findAllByTimeInterval(from, to);

        Map<LocalDate, Long> parts = costs
                .stream()
                .filter(cost -> cost.getCostType() == Cost.CostType.PART)
                .collect(Collectors.groupingBy(cost -> cost.getCreatedDate().toLocalDate(), Collectors.counting()));

        Map<LocalDate, Long> services = costs
                .stream()
                .filter(cost -> cost.getCostType() == Cost.CostType.SERVICE)
                .collect(Collectors.groupingBy(cost -> cost.getCreatedDate().toLocalDate(), Collectors.counting()));

        Set<LocalDate> allDates = new HashSet<>();
        allDates.addAll(parts.keySet());
        allDates.addAll(services.keySet());

        return allDates.stream()
                .map(date -> {
                    long partsCount = parts.getOrDefault(date, 0L);
                    long servicesCount = services.getOrDefault(date, 0L);

                    return new CostStatResponse(date, date.toString(),(int)
                            partsCount, (int) servicesCount);
                })
                .sorted(Comparator.comparing(CostStatResponse::getDate))
                .toList();
    }

    @Override
    public List<CostStatResponse> getWeekly(LocalDate from, LocalDate to) {
        List<Cost> costs = costRepository.findAllByTimeInterval(from, to);

        Map<LocalDate, Long> parts = costs
                .stream()
                .filter(cost -> cost.getCostType() == Cost.CostType.PART)
                .collect(Collectors.groupingBy(cost -> cost.getCreatedDate().toLocalDate(), Collectors.counting()));

        Map<LocalDate, Long> services = costs
                .stream()
                .filter(cost -> cost.getCostType() == Cost.CostType.SERVICE)
                .collect(Collectors.groupingBy(cost -> cost.getCreatedDate().toLocalDate(), Collectors.counting()));

        Set<LocalDate> allDates = new HashSet<>();
        allDates.addAll(parts.keySet());
        allDates.addAll(services.keySet());

        return allDates.stream()
                .collect(Collectors.groupingBy(
                        date -> date.get(WeekFields.ISO.weekOfYear()),
                        Collectors.toList()
                ))
                .values()
                .stream()
                .map(weekDates -> {
                    long partsCount = weekDates.stream()
                            .mapToLong(date -> parts.getOrDefault(date, 0L))
                            .sum();
                    long servicesCount = weekDates.stream()
                            .mapToLong(date -> services.getOrDefault(date, 0L))
                            .sum();

                    LocalDate weekStartDate = weekDates.get(0).with(WeekFields.ISO.dayOfWeek(), 1);
                    LocalDate weekEndDate = weekDates.get(0).with(WeekFields.ISO.dayOfWeek(), 7);

                    return new CostStatResponse(weekStartDate, weekStartDate.toString() + "-" + weekEndDate.toString(), (int) partsCount, (int) servicesCount);
                })
                .sorted(Comparator.comparing(CostStatResponse::getDate))
                .toList();
    }

    @Override
    public List<CostStatResponse> getMonthly(LocalDate from, LocalDate to) {
        List<Cost> costs = costRepository.findAllByTimeInterval(from, to);

        Map<LocalDate, Long> parts = costs
                .stream()
                .filter(cost -> cost.getCostType() == Cost.CostType.PART)
                .collect(Collectors.groupingBy(cost -> cost.getCreatedDate().toLocalDate(), Collectors.counting()));

        Map<LocalDate, Long> services = costs
                .stream()
                .filter(cost -> cost.getCostType() == Cost.CostType.SERVICE)
                .collect(Collectors.groupingBy(cost -> cost.getCreatedDate().toLocalDate(), Collectors.counting()));

        Set<LocalDate> allDates = new HashSet<>();
        allDates.addAll(parts.keySet());
        allDates.addAll(services.keySet());

        return allDates.stream()
                .collect(Collectors.groupingBy(
                        LocalDate::getMonth,
                        Collectors.toList()
                ))
                .values()
                .stream()
                .map(monthDates -> {
                    long partsCount = monthDates.stream()
                            .mapToLong(date -> parts.getOrDefault(date, 0L))
                            .sum();
                    long servicesCount = monthDates.stream()
                            .mapToLong(date -> services.getOrDefault(date, 0L))
                            .sum();

                    LocalDate monthStartDate = monthDates.get(0).withDayOfMonth(1);
                    LocalDate monthEndDate = monthDates.get(0).withDayOfMonth(monthDates.get(0).lengthOfMonth());

                    int monthNumber = monthDates.get(0).getMonthValue();

                    return new CostStatResponse(monthStartDate, monthStartDate.toString() + " - " + monthEndDate.toString(), (int) partsCount, (int) servicesCount);
                })
                .sorted(Comparator.comparing(CostStatResponse::getDate))
                .toList();
    }

    @Override
    public List<CostStatResponse> getYearly(LocalDate from, LocalDate to) {
        List<Cost> costs = costRepository.findAllByTimeInterval(from, to);

        Map<LocalDate, Long> parts = costs
                .stream()
                .filter(cost -> cost.getCostType() == Cost.CostType.PART)
                .collect(Collectors.groupingBy(cost -> cost.getCreatedDate().toLocalDate(), Collectors.counting()));

        Map<LocalDate, Long> services = costs
                .stream()
                .filter(cost -> cost.getCostType() == Cost.CostType.SERVICE)
                .collect(Collectors.groupingBy(cost -> cost.getCreatedDate().toLocalDate(), Collectors.counting()));

        Set<LocalDate> allDates = new HashSet<>();
        allDates.addAll(parts.keySet());
        allDates.addAll(services.keySet());


        return allDates.stream()
                .collect(Collectors.groupingBy(
                        date -> Year.of(date.getYear()),
                        Collectors.toList()
                ))
                .values()
                .stream()
                .map(yearDates -> {
                    long partsCount = yearDates.stream()
                            .mapToLong(date -> parts.getOrDefault(date, 0L))
                            .sum();
                    long servicesCount = yearDates.stream()
                            .mapToLong(date -> services.getOrDefault(date, 0L))
                            .sum();

                    LocalDate yearStartDate = yearDates.get(0).withDayOfYear(1);
                    LocalDate yearEndDate = yearDates.get(0).withDayOfYear(yearDates.get(0).lengthOfYear());

                    return new CostStatResponse(yearStartDate, yearStartDate.toString() + " - " + yearEndDate.toString(), (int) partsCount, (int) servicesCount);
                })
                .sorted(Comparator.comparing(CostStatResponse::getDate))
                .toList();
    }
}
