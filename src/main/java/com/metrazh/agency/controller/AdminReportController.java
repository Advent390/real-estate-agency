package com.metrazh.agency.controller;

import com.metrazh.agency.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

/**
 * Сторінка деталізованої аналітики. Аналог admin_reports() з app.py.
 * Дати приймаються як String (а не LocalDate напряму), бо форма з
 * незаповненими полями дат надсилає порожній рядок ("sold_start=") —
 * а не відсутній параметр. Якби тип аргументу був LocalDate,
 * Spring намагався б конвертувати "" у дату і падав з помилкою.
 */
@Controller
public class AdminReportController {

    private final ReportService reportService;

    public AdminReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/admin/reports")
    public String reports(
            @RequestParam(name = "sold_start", required = false) String soldStartRaw,
            @RequestParam(name = "sold_end", required = false) String soldEndRaw,
            @RequestParam(name = "view_start", required = false) String viewStartRaw,
            @RequestParam(name = "view_end", required = false) String viewEndRaw,
            Model model) {

        LocalDate soldStart = parseOrNull(soldStartRaw);
        LocalDate soldEnd = parseOrNull(soldEndRaw);
        LocalDate viewStart = parseOrNull(viewStartRaw);
        LocalDate viewEnd = parseOrNull(viewEndRaw);

        var soldByType = reportService.getSoldObjectsGroupedByType(soldStart, soldEnd);
        int totalSold = soldByType.values().stream().mapToInt(List::size).sum();
        var recentViewings = reportService.getRecentViewingsReport(viewStart, viewEnd);

        model.addAttribute("soldByType", soldByType);
        model.addAttribute("totalSold", totalSold);
        model.addAttribute("recentViewings", recentViewings);
        model.addAttribute("soldStart", soldStart);
        model.addAttribute("soldEnd", soldEnd);
        model.addAttribute("viewStart", viewStart);
        model.addAttribute("viewEnd", viewEnd);
        return "admin/reports";
    }

    private LocalDate parseOrNull(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(raw);
        } catch (Exception e) {
            return null;
        }
    }
}
