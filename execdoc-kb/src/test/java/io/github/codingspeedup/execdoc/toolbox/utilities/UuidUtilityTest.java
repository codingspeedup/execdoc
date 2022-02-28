package io.github.codingspeedup.execdoc.toolbox.utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UuidUtilityTest {

    @Test
    void nextUuid() {
        assertEquals(36, UuidUtility.nextUuid().length());
    }

    @Test
    void nextCompactUuid() {
        assertEquals(32, UuidUtility.nextCompactUuid().length());
    }

    @Test
    void nextFriendlyUuid() {
        assertEquals(22, UuidUtility.nextFriendlyUuid().length());
    }

}