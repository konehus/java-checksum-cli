package com.aries.utility;

public class TextColoring {

    public static final String RESET = "\033[0m";

    public static String cyan(String str) {
        return "\033[1;36m" + str + RESET;
    }

    public static String green(String str) {
        return "\033[1;32m" + str + RESET;
    }

    public static String yellow(String str) {
        return "\033[1;33m" + str + RESET;
    }

    public static String purple(String str) {
        return "\033[1;35m" + str + RESET;
    }

    public static String blue(String str) {
        return "\033[1;34m" + str + RESET;
    }

    public static String red(String str) {
        return "\033[1;31m" + str + RESET;
    }

    public static String white(String str) {
        return "\033[1;37m" + str + RESET;
    }

}
