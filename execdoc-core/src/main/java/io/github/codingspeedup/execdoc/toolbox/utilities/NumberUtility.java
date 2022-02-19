package io.github.codingspeedup.execdoc.toolbox.utilities;

public class NumberUtility {



    public static String toStringOrNull(Number value) {
        return value == null ? null : value.toString();
    }

}
