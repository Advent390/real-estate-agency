package com.metrazh.agency.advice;

import com.metrazh.agency.util.FlashMessage;
import com.metrazh.agency.util.FlashService;
import com.metrazh.agency.util.SessionKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.Year;
import java.util.List;

@ControllerAdvice
public class GlobalModelAdvice {

    private final FlashService flashService;

    public GlobalModelAdvice(FlashService flashService) {
        this.flashService = flashService;
    }

    @ModelAttribute("isClientLogged")
    public boolean isClientLogged(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(SessionKeys.CLIENT_ID) != null;
    }

    @ModelAttribute("isAdminLogged")
    public boolean isAdminLogged(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(SessionKeys.ADMIN_ID) != null;
    }

    @ModelAttribute("currentYear")
    public int currentYear() {
        return Year.now().getValue();
    }

    @ModelAttribute("flashMessages")
    public List<FlashMessage> flashMessages(HttpServletRequest request) {
        return flashService.consume(request);
    }
}
