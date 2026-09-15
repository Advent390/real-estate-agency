package com.metrazh.agency.web;

public final class ViewNames {

    private ViewNames() {
    }

    // --- Публічна частина сайту (view) ---
    public static final String CATALOG = "catalog";
    public static final String OBJECT_DETAIL = "object";
    public static final String LOGIN = "login";
    public static final String CABINET = "cabinet";

    // --- Адмінка (view) ---
    public static final String ADMIN_LOGIN = "admin/login";
    public static final String ADMIN_DASHBOARD = "admin/dashboard";
    public static final String ADMIN_FORM = "admin/form";
    public static final String ADMIN_VIEWINGS = "admin/viewings";
    public static final String ADMIN_CLIENTS = "admin/clients";
    public static final String ADMIN_REPORTS = "admin/reports";

    // --- Статичні redirect-адреси ---
    public static final String REDIRECT_HOME = "redirect:/";
    public static final String REDIRECT_LOGIN = "redirect:/login";
    public static final String REDIRECT_CABINET = "redirect:/cabinet";
    public static final String REDIRECT_ADMIN = "redirect:/admin";
    public static final String REDIRECT_ADMIN_LOGIN = "redirect:/admin/login";
    public static final String REDIRECT_ADMIN_VIEWINGS = "redirect:/admin/viewings";

    // --- Динамічні redirect-адреси (з параметром у шляху) ---

    public static String redirectToObject(Integer objectId) {
        return "redirect:/object/" + objectId;
    }

    /** Редірект на referer, якщо він є, інакше на fallback-адресу. */
    public static String redirectToRefererOr(String referer, String fallback) {
        return "redirect:" + (referer != null ? referer : fallback);
    }
}
