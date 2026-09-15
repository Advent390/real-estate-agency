package com.metrazh.agency.controller;

import com.metrazh.agency.dto.SearchFilters;
import com.metrazh.agency.entity.RealEstate;
import com.metrazh.agency.service.FavoriteService;
import com.metrazh.agency.service.RealEstateService;
import com.metrazh.agency.util.SessionKeys;
import com.metrazh.agency.web.ViewNames;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

/** Каталог для клієнта.*/
@Controller
public class CatalogController {

    private final RealEstateService realEstateService;
    private final FavoriteService favoriteService;

    public CatalogController(RealEstateService realEstateService, FavoriteService favoriteService) {
        this.realEstateService = realEstateService;
        this.favoriteService = favoriteService;
    }

    /** Головна сторінка — каталог з фільтрами. Аналог index(). */
    @GetMapping("/")
    public String index(@RequestParam(name = "type_id", required = false) Integer typeId,
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

        model.addAttribute("objects", realEstateService.search(filters));
        model.addAttribute("types", realEstateService.getObjectTypes());
        model.addAttribute("districts", realEstateService.getDistricts());
        model.addAttribute("filters", filters);
        return ViewNames.CATALOG;
    }

    /** Картка одного об'єкта. Аналог object_detail(). */
    @GetMapping("/object/{objectId}")
    public String objectDetail(@PathVariable Integer objectId, Model model, HttpServletRequest request) {
        RealEstate obj;
        try {
            obj = realEstateService.getById(objectId);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        HttpSession session = request.getSession(false);
        boolean inFavorites = false;
        if (session != null && session.getAttribute(SessionKeys.CLIENT_ID) != null) {
            Integer clientId = (Integer) session.getAttribute(SessionKeys.CLIENT_ID);
            inFavorites = favoriteService.isInFavorites(clientId, objectId);
        }

        model.addAttribute("obj", obj);
        model.addAttribute("photos", realEstateService.getPhotos(objectId));
        model.addAttribute("inFavorites", inFavorites);
        return ViewNames.OBJECT_DETAIL;
    }
}
