package com.metrazh.agency.controller;

import com.metrazh.agency.entity.Client;
import com.metrazh.agency.service.AuthService;
import com.metrazh.agency.util.FlashService;
import com.metrazh.agency.util.SessionKeys;
import com.metrazh.agency.web.ViewNames;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * Реєстрація / логін / логаут клієнта.
 * Аналог розділу "РЕЄСТРАЦІЯ / ЛОГІН КЛІЄНТА" з app.py.
 */
@Controller
public class AuthController {

    private final AuthService authService;
    private final FlashService flashService;

    public AuthController(AuthService authService, FlashService flashService) {
        this.authService = authService;
        this.flashService = flashService;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("mode", "register");
        return ViewNames.LOGIN;
    }

    @PostMapping("/register")
    public String register(@RequestParam String email,
                            @RequestParam String password,
                            @RequestParam("full_name") String fullName,
                            @RequestParam(required = false, defaultValue = "") String phone,
                            HttpServletRequest request,
                            Model model) {
        AuthService.RegistrationResult result = authService.registerClient(
                email.strip().toLowerCase(), password, fullName.strip(), phone.strip());

        if (result.success()) {
            HttpSession session = request.getSession(true);
            session.setAttribute(SessionKeys.CLIENT_ID, result.client().getClientId());
            session.setAttribute(SessionKeys.CLIENT_NAME, result.client().getFullName());
            flashService.flash(request, "success", "Реєстрація успішна!");
            return ViewNames.REDIRECT_CABINET;
        }

        flashService.flash(request, "danger", result.errorMessage());
        model.addAttribute("mode", "register");
        return ViewNames.LOGIN;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("mode", "login");
        return ViewNames.LOGIN;
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                         @RequestParam String password,
                         HttpServletRequest request,
                         Model model) {
        Optional<Client> client = authService.loginClient(email.strip().toLowerCase(), password);

        if (client.isPresent()) {
            HttpSession session = request.getSession(true);
            session.setAttribute(SessionKeys.CLIENT_ID, client.get().getClientId());
            session.setAttribute(SessionKeys.CLIENT_NAME, client.get().getFullName());
            flashService.flash(request, "success", "Вітаємо, " + client.get().getFullName() + "!");
            return ViewNames.REDIRECT_CABINET;
        }

        flashService.flash(request, "danger", "Невірний email або пароль");
        model.addAttribute("mode", "login");
        return ViewNames.LOGIN;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(SessionKeys.CLIENT_ID);
            session.removeAttribute(SessionKeys.CLIENT_NAME);
        }
        flashService.flash(request, "info", "Ви вийшли з акаунта");
        return ViewNames.REDIRECT_HOME;
    }
}
