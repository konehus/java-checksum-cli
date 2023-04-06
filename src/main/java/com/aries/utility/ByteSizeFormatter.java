package com.aries.utility;

import java.text.DecimalFormat;

public class ByteSizeFormatter {

    public static String humanReadableByteCountSI(long bytes) {
        if (bytes < 1000) {
            return bytes + " B";
        }
        final String[] units = {"kB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
        int exp = (int) (Math.log(bytes) / Math.log(1000));
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(bytes / Math.pow(1000, exp)) + " " + units[exp - 1];
    }
}
