package com.metrazh.agency.controller;

import com.metrazh.agency.entity.AdminUser;
import com.metrazh.agency.service.AuthService;
import com.metrazh.agency.util.FlashService;
import com.metrazh.agency.util.SessionKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/** Логін/логаут адміністратора. Аналог розділу "АДМІНКА — ЛОГІН" з app.py. */
@Controller
public class AdminAuthController {

    private final AuthService authService;
    private final FlashService flashService;

    public AdminAuthController(AuthService authService, FlashService flashService) {
        this.authService = authService;
        this.flashService = flashService;
    }

    @GetMapping("/admin/login")
    public String loginForm() {
        return "admin/login";
    }

    @PostMapping("/admin/login")
    public String login(@RequestParam String username,
                         @RequestParam String password,
                         HttpServletRequest request) {
        Optional<AdminUser> user = authService.loginAdmin(username.strip(), password);

        if (user.isPresent()) {
            HttpSession session = request.getSession(true);
            session.setAttribute(SessionKeys.ADMIN_ID, user.get().getUserId());
            session.setAttribute(SessionKeys.ADMIN_NAME, user.get().getFullName());
            flashService.flash(request, "success", "Вітаємо, " + user.get().getFullName() + "!");
            return "redirect:/admin";
        }

        flashService.flash(request, "danger", "Невірні дані");
        return "admin/login";
    }

    @GetMapping("/admin/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(SessionKeys.ADMIN_ID);
            session.removeAttribute(SessionKeys.ADMIN_NAME);
        }
        return "redirect:/admin/login";
    }
}
