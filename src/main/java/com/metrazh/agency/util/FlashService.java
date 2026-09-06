package com.metrazh.agency.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Сесійний аналог Flask flash()/get_flashed_messages(with_categories=true).
 * Повідомлення живуть у сесії до першого читання (base.html читає й одразу
 * очищає їх через {@link #consume(HttpServletRequest)}), тому працюють
 * однаково і з контролерів, і з interceptor'ів (де немає RedirectAttributes).
 */
@Component
public class FlashService {

    private static final String SESSION_KEY = "flashMessages";

    public void flash(HttpServletRequest request, String category, String text) {
        HttpSession session = request.getSession(true);
        @SuppressWarnings("unchecked")
        List<FlashMessage> messages = (List<FlashMessage>) session.getAttribute(SESSION_KEY);
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(new FlashMessage(category, text));
        session.setAttribute(SESSION_KEY, messages);
    }

    /** Повертає накопичені повідомлення і одразу очищає їх у сесії. */
    public List<FlashMessage> consume(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return List.of();
        }
        @SuppressWarnings("unchecked")
        List<FlashMessage> messages = (List<FlashMessage>) session.getAttribute(SESSION_KEY);
        session.removeAttribute(SESSION_KEY);
        return messages != null ? messages : List.of();
    }
}
