package com.metrazh.agency.service;

import com.metrazh.agency.entity.RealEstate;
import com.metrazh.agency.entity.Viewing;
import com.metrazh.agency.repository.RealEstateRepository;
import com.metrazh.agency.repository.ViewingRepository;
import com.metrazh.agency.specification.RealEstateSpecifications;
import com.metrazh.agency.specification.ViewingSpecifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Звіти для адмін-панелі.
 * Аналог get_detailed_sold_objects() та get_recent_viewings_report() з db.py.
 * Фільтри по датах будуються через Specifications (Criteria API), а не
 * через JPQL з "CAST(:param AS timestamp) IS NULL OR ..." — останній варіант
 * ламається на PostgreSQL/Hibernate 6, коли параметр дорівнює null
 * (драйвер не завжди вгадує тип параметра всередині CAST).
 */
@Service
@Transactional(readOnly = true)
public class ReportService {

    private final RealEstateRepository realEstateRepository;
    private final ViewingRepository viewingRepository;

    public ReportService(RealEstateRepository realEstateRepository, ViewingRepository viewingRepository) {
        this.realEstateRepository = realEstateRepository;
        this.viewingRepository = viewingRepository;
    }

    /** Продані об'єкти, згруповані за типом (для reports.html). */
    public Map<String, List<RealEstate>> getSoldObjectsGroupedByType(LocalDate start, LocalDate end) {
        LocalDateTime startDt = start != null ? start.atStartOfDay() : null;
        LocalDateTime endDt = end != null ? end.atTime(LocalTime.MAX) : null;

        List<RealEstate> sold = realEstateRepository.findAll(RealEstateSpecifications.soldBetween(startDt, endDt));

        Map<String, List<RealEstate>> grouped = new LinkedHashMap<>();
        for (RealEstate re : sold) {
            grouped.computeIfAbsent(re.getType().getName(), k -> new java.util.ArrayList<>()).add(re);
        }
        return grouped;
    }

    /**
     * Заявки на перегляд за період. Якщо start не вказано — останні 7 днів
     * (так само, як в оригіналі, коли дати не обрані на формі).
     */
    public List<Viewing> getRecentViewingsReport(LocalDate start, LocalDate end) {
        LocalDateTime startDt = start != null ? start.atStartOfDay() : LocalDateTime.now().minusDays(7);
        LocalDateTime endDt = end != null ? end.atTime(LocalTime.MAX) : null;
        return viewingRepository.findAll(ViewingSpecifications.dateBetween(startDt, endDt));
    }
}
