package com.metrazh.agency.controller;

import com.metrazh.agency.dto.SearchFilters;
import com.metrazh.agency.service.RealEstateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/** Дашборд адміна зі списком і фільтрами всіх об'єктів (включно з архівом). */
@Controller
public class AdminDashboardController {

    private final RealEstateService realEstateService;

    public AdminDashboardController(RealEstateService realEstateService) {
        this.realEstateService = realEstateService;
    }

    @GetMapping("/admin")
    public String dashboard(@RequestParam(name = "type_id", required = false) Integer typeId,
                             @RequestParam(name = "district_id", required = false) Integer districtId,
                             @RequestParam(name = "status_id", required = false) Integer statusId,
                             @RequestParam(name = "rooms", required = false) Integer rooms,
                             @RequestParam(name = "min_price", required = false) BigDecimal minPrice,
                             @RequestParam(name = "max_price", required = false) BigDecimal maxPrice,
                             @RequestParam(name = "min_area", required = false) BigDecimal minArea,
                             @RequestParam(name = "max_area", required = false) BigDecimal maxArea,
                             Model model) {

        SearchFilters filters = new SearchFilters();
        filters.setTypeId(typeId);
        filters.setDistrictId(districtId);
        filters.setStatusId(statusId);
        filters.setRooms(rooms);
        filters.setMinPrice(minPrice);
        filters.setMaxPrice(maxPrice);
        filters.setMinArea(minArea);
        filters.setMaxArea(maxArea);
        filters.setIncludeArchived(true); // адмін бачить і архів, як в оригіналі

        model.addAttribute("objects", realEstateService.search(filters));
        model.addAttribute("types", realEstateService.getObjectTypes());
        model.addAttribute("districts", realEstateService.getDistricts());
        model.addAttribute("statuses", realEstateService.getStatuses());
        model.addAttribute("stats", realEstateService.getStats());
        model.addAttribute("filters", filters);
        return "admin/dashboard";
    }
}
