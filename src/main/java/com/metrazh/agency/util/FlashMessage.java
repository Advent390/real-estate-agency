package com.metrazh.agency.util;

import java.io.Serializable;

/**
 * Аналог Flask flash(message, category). Категорії такі самі, як в
 * оригіналі: success / danger / info / warning.
 * Контролери додають об'єкт через RedirectAttributes.addFlashAttribute("flash", ...),
 * а base.html читає атрибут "flash" з моделі (рендериться лише один раз, після редіректу).
 */
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
