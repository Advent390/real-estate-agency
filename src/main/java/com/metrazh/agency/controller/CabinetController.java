package com.metrazh.agency.controller;

import com.metrazh.agency.service.FavoriteService;
import com.metrazh.agency.service.ViewingService;
import com.metrazh.agency.util.FlashService;
import com.metrazh.agency.util.SessionKeys;
import com.metrazh.agency.web.ViewNames;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Особистий кабінет клієнта: обране, заявки на перегляд.
 * Ці маршрути захищені ClientAuthInterceptor.
 */
@Controller
public class CabinetController {

    private static final DateTimeFormatter VIEWING_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private final ViewingService viewingService;
    private final FavoriteService favoriteService;
    private final FlashService flashService;

    public CabinetController(ViewingService viewingService, FavoriteService favoriteService,
                              FlashService flashService) {
        this.viewingService = viewingService;
        this.favoriteService = favoriteService;
        this.flashService = flashService;
    }

    @RequestMapping("/cabinet")
    public String cabinet(HttpServletRequest request, Model model) {
        Integer clientId = (Integer) request.getSession(true).getAttribute(SessionKeys.CLIENT_ID);
        model.addAttribute("viewings", viewingService.getClientViewings(clientId));
        model.addAttribute("favorites", favoriteService.getClientFavorites(clientId)
                .stream().map(f -> f.getRealEstate()).toList());
        return ViewNames.CABINET;
    }

    /** Створення заявки на перегляд. Аналог request_viewing(). */
    @PostMapping("/object/{objectId}/viewing")
    public String requestViewing(@PathVariable Integer objectId,
                                  @RequestParam("viewing_date") String viewingDateRaw,
                                  @RequestParam(required = false, defaultValue = "") String comment,
                                  HttpServletRequest request) {
        LocalDateTime viewingDate;
        try {
            viewingDate = LocalDateTime.parse(viewingDateRaw, VIEWING_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            flashService.flash(request, "danger", "Невірний формат дати");
            return ViewNames.redirectToObject(objectId);
        }

        Integer clientId = (Integer) request.getSession(true).getAttribute(SessionKeys.CLIENT_ID);
        viewingService.createViewing(clientId, objectId, viewingDate, comment.strip());
        flashService.flash(request, "success", "Заявка на перегляд створена. Очікуйте підтвердження.");
        return ViewNames.REDIRECT_CABINET;
    }

    /** Додати/прибрати з обраного. Аналог toggle_favorite(). */
    @PostMapping("/object/{objectId}/favorite")
    public String toggleFavorite(@PathVariable Integer objectId, HttpServletRequest request) {
        Integer clientId = (Integer) request.getSession(true).getAttribute(SessionKeys.CLIENT_ID);

        if (favoriteService.isInFavorites(clientId, objectId)) {
            favoriteService.removeFromFavorites(clientId, objectId);
            flashService.flash(request, "info", "Видалено з обраного");
        } else {
            favoriteService.addToFavorites(clientId, objectId);
            flashService.flash(request, "success", "Додано в обране");
        }

        String referer = request.getHeader("Referer");
        return ViewNames.redirectToRefererOr(referer, "/object/" + objectId);
    }
}
