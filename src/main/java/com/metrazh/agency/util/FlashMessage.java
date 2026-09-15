package com.metrazh.agency.util;

import java.io.Serializable;

public class FlashMessage implements Serializable {

    private final String category;
    private final String text;

    public FlashMessage(String category, String text) {
        this.category = category;
        this.text = text;
    }

    public String getCategory() {
        return category;
    }

    public String getText() {
        return text;
    }
}
