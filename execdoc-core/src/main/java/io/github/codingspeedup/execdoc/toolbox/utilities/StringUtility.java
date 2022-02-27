package io.github.codingspeedup.execdoc.toolbox.utilities;

import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class StringUtility {

    private static final URLCodec URL_CODEC = new URLCodec();

    public static String[] splitLines(String string) {
        return string.split("\\r?\\n");
    }

    public static String encodeBase64(byte[] byteArray) {
        return Base64.encodeBase64String(byteArray);
    }

    public static byte[] decodeBase64(String base64Encoding) {
        return Base64.decodeBase64(base64Encoding);
    }

    @SneakyThrows
    public static String urlEncode(String value) {
        return URL_CODEC.encode(value);
    }

}
