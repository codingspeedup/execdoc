package io.github.codingspeedup.execdoc.generators.utilities;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Locale;

public class GenUtility {

    public static String toBasicL10nKey(String label) {
        label = StringUtils.stripAccents(label).toLowerCase(Locale.ROOT);
        label = label.replaceAll("[^a-z0-9\\s]", "");
        label = label.trim().replaceAll("\\s+", "-");
        int maxLength = 76;
        if (label.length() > maxLength) {
            if (label.charAt(maxLength - 1) == '-') {
                --maxLength;
            }
            label = label.substring(0, maxLength);
        }
        return label;
    }


    public static String simpleQuote(String string) {
        if (string == null) {
            return null;
        }
        if (string.contains("'")) {
            throw new UnsupportedOperationException("Escaping \"'\" is not specified");
        }
        return "'" + string + "'";
    }

    public static String joinPackageName(String... parts) {
        StringBuilder packageNameBuilder = new StringBuilder();
        if (ArrayUtils.isNotEmpty(parts)) {
            for (String part : parts) {
                packageNameBuilder.append(StringUtils.trimToEmpty(part)).append(".");
            }
        }
        String packageName = packageNameBuilder.toString().replaceAll("\\.+", ".");
        if (packageName.startsWith(".")) {
            packageName = packageName.substring(1);
        }
        if (packageName.endsWith(".")) {
            packageName = packageName.substring(0, packageName.length() - 1);
        }
        return packageName;
    }

    public static File fileOf(File root, String packageName, String fileName) {
        if (StringUtils.isNotBlank(packageName)) {
            root = new File(root, packageName.replaceAll("\\.", "/"));
        }
        return new File(root, fileName);
    }

}
