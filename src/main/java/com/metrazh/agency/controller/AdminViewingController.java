package com.metrazh.agency.controller;

import com.metrazh.agency.service.ViewingService;
import com.metrazh.agency.util.FlashService;
import com.metrazh.agency.web.ViewNames;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Аналог admin_viewings() / admin_viewing_status() з app.py. */
@Controller
public class AdminViewingController {

    private final ViewingService viewingService;
    private final FlashService flashService;

    public AdminViewingController(ViewingService viewingService, FlashService flashService) {
        this.viewingService = viewingService;
        this.flashService = flashService;
    }

    @GetMapping("/admin/viewings")
    public String list(Model model) {
        model.addAttribute("viewings", viewingService.getAllViewings());
        return ViewNames.ADMIN_VIEWINGS;
    }

    @PostMapping("/admin/viewings/{viewingId}/status")
    public String updateStatus(@PathVariable Integer viewingId,
                                @RequestParam("request_status") String requestStatus,
                                HttpServletRequest request) {
        viewingService.updateStatus(viewingId, requestStatus);
        flashService.flash(request, "success", "Статус заявки оновлено");
        return ViewNames.REDIRECT_ADMIN_VIEWINGS;
    }
}
