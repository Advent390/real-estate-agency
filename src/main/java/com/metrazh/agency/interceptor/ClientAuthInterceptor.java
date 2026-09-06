package com.metrazh.agency.interceptor;

import com.metrazh.agency.util.FlashService;
import com.metrazh.agency.util.SessionKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Аналог декоратора @client_required з auth.py: маршрут доступний
 * лише залогіненому клієнту, інакше — flash-повідомлення і редірект на /login.
 */
public class ClientAuthInterceptor implements HandlerInterceptor {

    private final FlashService flashService;

    public ClientAuthInterceptor(FlashService flashService) {
        this.flashService = flashService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession(false);
        boolean loggedIn = session != null && session.getAttribute(SessionKeys.CLIENT_ID) != null;

        if (!loggedIn) {
            flashService.flash(request, "warning", "Будь ласка, увійдіть або зареєструйтесь");
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }
}
