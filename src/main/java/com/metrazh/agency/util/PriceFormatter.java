package com.metrazh.agency.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Аналог @app.template_filter('price') з app.py:
 * f"{int(value):,} грн".replace(',', ' ')
 * У шаблонах Thymeleaf викликається як ${@priceFormatter.format(obj.price)}.
 */
@Component("priceFormatter")
public class PriceFormatter {

    public String format(BigDecimal value) {
        if (value == null) {
            return "";
        }
        long rounded = value.setScale(0, RoundingMode.HALF_UP).longValue();
        String digits = Long.toString(rounded);

        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = digits.length() - 1; i >= 0; i--) {
            sb.append(digits.charAt(i));
            count++;
            if (count % 3 == 0 && i != 0) {
                sb.append(' ');
            }
        }
        return sb.reverse() + " грн";
    }
}
