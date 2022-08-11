package io.github.codingspeedup.execdoc.toolbox.utilities;

import org.junit.jupiter.api.Test;

class OsUtilityTest {

    @Test
    void browse() {
        OsUtility.browse("https://www.google.com");
    }

    @Test
    void copyToSystemClipboard() {
        OsUtility.copyToSystemClipboard("Lorem ipsum...");
    }

}
