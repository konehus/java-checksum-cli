package com.aries.utility;

import java.text.DecimalFormat;

public class ChecksumUtility {

    public static String humanReadableByteCountSI(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        DecimalFormat df = new DecimalFormat("#.##");
        String[] units = {"B", "kB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
        int exp = (int) (Math.log(bytes) / Math.log(1000));
        return df.format(bytes / Math.pow(1000, exp)) + " " + units[exp];
    }
}