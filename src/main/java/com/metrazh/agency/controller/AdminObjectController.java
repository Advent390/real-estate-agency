package com.metrazh.agency.controller;

import com.metrazh.agency.dto.RealEstateFormData;
import com.metrazh.agency.entity.RealEstate;
import com.metrazh.agency.service.RealEstateService;
import com.metrazh.agency.util.FlashService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * CRUD об'єктів нерухомості в адмінці.
 * Аналог admin_object_new / admin_object_edit / admin_object_delete /
 * admin_object_status / _parse_object_form з app.py.
 */
@Controller
@RequestMapping("/admin/object")
public class AdminObjectController {

    private final RealEstateService realEstateService;
    private final FlashService flashService;

    public AdminObjectController(RealEstateService realEstateService, FlashService flashService) {
        this.realEstateService = realEstateService;
        this.flashService = flashService;
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        addReferenceData(model, null);
        return "admin/form";
    }

    @PostMapping("/new")
    public String create(HttpServletRequest request, Model model) {
        try {
            RealEstateFormData data = parseForm(request);
            RealEstate saved = realEstateService.create(data);
            flashService.flash(request, "success", "Об'єкт #" + saved.getObjectId() + " створено");
            return "redirect:/admin";
        } catch (Exception e) {
            flashService.flash(request, "danger", "Помилка: " + HtmlUtils.htmlEscape(e.getMessage()));
            addReferenceData(model, null);
            return "admin/form";
        }
    }

    @GetMapping("/{objectId}/edit")
    public String editForm(@PathVariable Integer objectId, Model model) {
        RealEstate obj;
        try {
            obj = realEstateService.getById(objectId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        addReferenceData(model, obj);
        return "admin/form";
    }

    @PostMapping("/{objectId}/edit")
    public String update(@PathVariable Integer objectId, HttpServletRequest request, Model model) {
        try {
            RealEstateFormData data = parseForm(request);
            realEstateService.update(objectId, data);
            flashService.flash(request, "success", "Об'єкт #" + objectId + " оновлено");
            return "redirect:/admin";
        } catch (Exception e) {
            flashService.flash(request, "danger", "Помилка: " + HtmlUtils.htmlEscape(e.getMessage()));
            try {
                addReferenceData(model, realEstateService.getById(objectId));
            } catch (EntityNotFoundException ignored) {
                addReferenceData(model, null);
            }
            return "admin/form";
        }
    }

    @PostMapping("/{objectId}/delete")
    public String delete(@PathVariable Integer objectId, HttpServletRequest request) {
        realEstateService.delete(objectId);
        flashService.flash(request, "info", "Об'єкт #" + objectId + " видалено");
        return "redirect:/admin";
    }

    @PostMapping("/{objectId}/status")
    public String updateStatus(@PathVariable Integer objectId,
                                @RequestParam("status_id") Integer statusId,
                                HttpServletRequest request) {
        realEstateService.updateStatus(objectId, statusId);
        flashService.flash(request, "success", "Статус оновлено");
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/admin");
    }

    private void addReferenceData(Model model, RealEstate obj) {
        model.addAttribute("obj", obj);
        model.addAttribute("types", realEstateService.getObjectTypes());
        model.addAttribute("districts", realEstateService.getDistricts());
        model.addAttribute("statuses", realEstateService.getStatuses());
    }

    /** Аналог _parse_object_form(form) з app.py. */
    private RealEstateFormData parseForm(HttpServletRequest request) {
        Map<String, String[]> p = request.getParameterMap();

        RealEstateFormData data = new RealEstateFormData();
        int typeId = Integer.parseInt(single(p, "type_id"));
        data.setTypeId(typeId);
        data.setDistrictId(Integer.parseInt(single(p, "district_id")));
        data.setStatusId(Integer.parseInt(single(p, "status_id")));
        data.setStreet(single(p, "street").strip());
        data.setHouseNumber(single(p, "house_number").strip());
        String apartmentNumber = singleOrEmpty(p, "apartment_number").strip();
        data.setApartmentNumber(apartmentNumber.isEmpty() ? null : apartmentNumber);
        data.setTotalArea(new BigDecimal(single(p, "total_area")));
        data.setPrice(new BigDecimal(single(p, "price")));
        data.setDescription(singleOrEmpty(p, "description").strip());
        String mainPhoto = singleOrEmpty(p, "main_photo").strip();
        data.setMainPhoto(mainPhoto.isEmpty() ? "/img/placeholder.jpg" : mainPhoto);

        switch (typeId) {
            case 1 -> { // Квартира
                data.setRooms(Integer.parseInt(single(p, "rooms")));
                data.setFloor(Integer.parseInt(single(p, "floor")));
                data.setTotalFloors(Integer.parseInt(single(p, "total_floors")));
            }
            case 2 -> { // Будинок
                data.setRooms(Integer.parseInt(single(p, "rooms")));
                data.setTotalFloors(Integer.parseInt(single(p, "total_floors")));
                data.setPlotArea(new BigDecimal(single(p, "plot_area")));
            }
            case 3 -> { // Офіс
                data.setRoomsCount(Integer.parseInt(single(p, "rooms_count")));
                data.setFloor(Integer.parseInt(single(p, "floor")));
                data.setTotalFloors(Integer.parseInt(single(p, "total_floors")));
                data.setPurpose(single(p, "purpose").strip());
            }
            default -> throw new IllegalArgumentException("Невідомий тип об'єкта");
        }
        return data;
    }

    private String single(Map<String, String[]> params, String key) {
        String[] values = params.get(key);
        if (values == null || values.length == 0 || values[0] == null) {
            throw new IllegalArgumentException("Поле '" + key + "' є обов'язковим");
        }
        return values[0];
    }

    private String singleOrEmpty(Map<String, String[]> params, String key) {
        String[] values = params.get(key);
        return (values == null || values.length == 0 || values[0] == null) ? "" : values[0];
    }
}
