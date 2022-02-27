package io.github.codingspeedup.execdoc.toolbox.utilities;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringUtilityTest {

    @Test
    public void splitLines() {
        assertEquals(1, StringUtility.splitLines("").length);
        assertEquals(2, StringUtility.splitLines("Lorem\nipsum").length);
        assertEquals(2, StringUtility.splitLines("Lorem\r\nipsum").length);
    }

}