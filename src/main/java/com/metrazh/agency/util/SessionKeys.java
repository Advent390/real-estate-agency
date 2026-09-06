package com.metrazh.agency.util;

/** Ключі атрибутів HttpSession. Аналог session['client_id'] тощо з Flask. */
public final class SessionKeys {

    public static final String CLIENT_ID = "clientId";
    public static final String CLIENT_NAME = "clientName";
    public static final String ADMIN_ID = "adminId";
    public static final String ADMIN_NAME = "adminName";

    private SessionKeys() {
    }
}
