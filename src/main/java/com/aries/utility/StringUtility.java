package com.aries.utility;

import java.util.Locale;

public class StringUtility {

    public static String formatWithLocale(String format, Object... args) {
        return String.format(Locale.ROOT, format, args);
    }
}
