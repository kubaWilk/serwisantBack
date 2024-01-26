package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.controller.statistics.RepairStatResponse;
import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import com.jakubwilk.serwisant.api.repository.RepairRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class RepairStatisticsService implements StatisticsService<RepairStatResponse>{
    private final RepairRepository repairRepository;

    @Override
    public List<RepairStatResponse> getDaily(LocalDate from, LocalDate to) {
        List<Repair> repairsOpened =
                repairRepository.findAllOpenByTimeInterval(from, to);
        List<Repair> repairsClosed =
                repairRepository.findAllClosedByTimeInterval(from, to);

        Map<LocalDate, Long> opened = repairsOpened
                .stream()
                .collect(Collectors.groupingBy(repair ->
                        repair.getCreatedDate()
                                .toLocalDate(), Collectors.counting()));

        Map<LocalDate, Long> closed = repairsClosed
                .stream()
                .collect(Collectors.groupingBy(
                        repair -> repair.getClosedAt()
                                .toLocalDate(), Collectors.counting()));

        Set<LocalDate> allDates = new HashSet<>();
        allDates.addAll(opened.keySet());
        allDates.addAll(closed.keySet());

        return allDates.stream()
                .map(date -> {
                    long openedCount = opened.getOrDefault(date, 0L);
                    long closedCount = closed.getOrDefault(date, 0L);

                    return new RepairStatResponse(date, date.toString(),(int)
                            openedCount, (int) closedCount);
                })
                .sorted(Comparator.comparing(RepairStatResponse::getDate))
                .toList();
    }

    @Override
    public List<RepairStatResponse> getWeekly(LocalDate from, LocalDate to) {
        List<Repair> repairsOpened =
                repairRepository.findAllOpenByTimeInterval(from, to);
        List<Repair> repairsClosed =
                repairRepository.findAllClosedByTimeInterval(from, to);

        Map<LocalDate, Long> opened = repairsOpened
                .stream()
                .collect(Collectors.groupingBy(repair ->
                        repair.getCreatedDate()
                                .toLocalDate(), Collectors.counting()));

        Map<LocalDate, Long> closed = repairsClosed
                .stream()
                .collect(Collectors.groupingBy(repair -> repair.getClosedAt().toLocalDate(), Collectors.counting()));

        Set<LocalDate> allDates = new HashSet<>();
        allDates.addAll(opened.keySet());
        allDates.addAll(closed.keySet());

        return allDates.stream()
                .collect(Collectors.groupingBy(
                        date -> date.get(WeekFields.ISO.weekOfYear()),
                        Collectors.toList()
                ))
                .values()
                .stream()
                .map(weekDates -> {
                    long openedCount = weekDates.stream()
                            .mapToLong(date -> opened.getOrDefault(date, 0L))
                            .sum();
                    long closedCount = weekDates.stream()
                            .mapToLong(date -> closed.getOrDefault(date, 0L))
                            .sum();

                    LocalDate weekStartDate = weekDates.get(0).with(WeekFields.ISO.dayOfWeek(), 1);
                    LocalDate weekEndDate = weekDates.get(0).with(WeekFields.ISO.dayOfWeek(), 7);

                    return new RepairStatResponse(weekStartDate, weekStartDate.toString() + "-" + weekEndDate.toString(), (int) openedCount, (int) closedCount);
                })
                .sorted(Comparator.comparing(RepairStatResponse::getDate))
                .toList();
    }

    @Override
    public List<RepairStatResponse> getMonthly(LocalDate from, LocalDate to) {
        List<Repair> repairsOpened = repairRepository.findAllOpenByTimeInterval(from, to);
        List<Repair> repairsClosed = repairRepository.findAllClosedByTimeInterval(from, to);

        Map<LocalDate, Long> opened = repairsOpened
                .stream()
                .collect(Collectors.groupingBy(repair -> repair.getCreatedDate().toLocalDate(), Collectors.counting()));

        Map<LocalDate, Long> closed = repairsClosed
                .stream()
                .collect(Collectors.groupingBy(repair -> repair.getClosedAt().toLocalDate(), Collectors.counting()));

        Set<LocalDate> allDates = new HashSet<>();
        allDates.addAll(opened.keySet());
        allDates.addAll(closed.keySet());

        return allDates.stream()
                .collect(Collectors.groupingBy(
                        LocalDate::getMonth,
                        Collectors.toList()
                ))
                .values()
                .stream()
                .map(monthDates -> {
                    long openedCount = monthDates.stream()
                            .mapToLong(date -> opened.getOrDefault(date, 0L))
                            .sum();
                    long closedCount = monthDates.stream()
                            .mapToLong(date -> closed.getOrDefault(date, 0L))
                            .sum();

                    LocalDate monthStartDate = monthDates.get(0).withDayOfMonth(1);
                    LocalDate monthEndDate = monthDates.get(0).withDayOfMonth(monthDates.get(0).lengthOfMonth());

                    int monthNumber = monthDates.get(0).getMonthValue();

                    return new RepairStatResponse(monthStartDate, monthStartDate.toString() + " - " + monthEndDate.toString(), (int) openedCount, (int) closedCount);
                })
                .sorted(Comparator.comparing(RepairStatResponse::getDate))
                .toList();
    }

    @Override
    public List<RepairStatResponse> getYearly(LocalDate from, LocalDate to) {
        List<Repair> repairsOpened = repairRepository.findAllOpenByTimeInterval(from, to);
        List<Repair> repairsClosed = repairRepository.findAllClosedByTimeInterval(from, to);

        Map<LocalDate, Long> opened = repairsOpened
                .stream()
                .collect(Collectors.groupingBy(repair -> repair.getCreatedDate().toLocalDate(), Collectors.counting()));

        Map<LocalDate, Long> closed = repairsClosed
                .stream()
                .collect(Collectors.groupingBy(repair -> repair.getClosedAt().toLocalDate(), Collectors.counting()));

        Set<LocalDate> allDates = new HashSet<>();
        allDates.addAll(opened.keySet());
        allDates.addAll(closed.keySet());

        return allDates.stream()
                .collect(Collectors.groupingBy(
                        date -> Year.of(date.getYear()),
                        Collectors.toList()
                ))
                .values()
                .stream()
                .map(yearDates -> {
                    long openedCount = yearDates.stream()
                            .mapToLong(date -> opened.getOrDefault(date, 0L))
                            .sum();
                    long closedCount = yearDates.stream()
                            .mapToLong(date -> closed.getOrDefault(date, 0L))
                            .sum();

                    LocalDate yearStartDate = yearDates.get(0).withDayOfYear(1);
                    LocalDate yearEndDate = yearDates.get(0).withDayOfYear(yearDates.get(0).lengthOfYear());

                    return new RepairStatResponse(yearStartDate, yearStartDate.toString() + " - " + yearEndDate.toString(), (int) openedCount, (int) closedCount);
                })
                .sorted(Comparator.comparing(RepairStatResponse::getDate))
                .toList();
    }
}
