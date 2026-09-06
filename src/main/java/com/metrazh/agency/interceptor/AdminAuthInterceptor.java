package com.metrazh.agency.interceptor;

import com.metrazh.agency.util.FlashService;
import com.metrazh.agency.util.SessionKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Аналог декоратора @admin_required з auth.py: маршрут доступний
 * лише залогіненому адміну, інакше — flash-повідомлення і редірект на /admin/login.
 */
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final FlashService flashService;

    public AdminAuthInterceptor(FlashService flashService) {
        this.flashService = flashService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession(false);
        boolean loggedIn = session != null && session.getAttribute(SessionKeys.ADMIN_ID) != null;

        if (!loggedIn) {
            flashService.flash(request, "danger", "Доступ лише для адміністраторів");
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return false;
        }
        return true;
    }
}
