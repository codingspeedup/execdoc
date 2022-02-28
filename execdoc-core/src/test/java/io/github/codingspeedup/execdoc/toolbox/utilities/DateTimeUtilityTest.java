package io.github.codingspeedup.execdoc.toolbox.utilities;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateTimeUtilityTest {

    @Test
    void toIsoDateString() {
        assertEquals("2001-10-11", DateTimeUtility.toIsoDateString(LocalDate.of(2001, 10, 11)));
    }

}