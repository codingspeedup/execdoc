package io.github.codingspeedup.execdoc.generators.utilities;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class GenUtilityTest {

    @Test
    public void toBasicL10nKey() {
        assertEquals("1-lorem-ipsum", GenUtility.toBasicL10nKey("\t1 LorÈm ?  ipsum..\n."));
    }

    @Test
    void simpleQuote() {
        assertNull(GenUtility.simpleQuote(null));
        assertEquals("''", GenUtility.simpleQuote(""));
        assertEquals("' '", GenUtility.simpleQuote(" "));
        assertEquals("'\n'", GenUtility.simpleQuote("\n"));
        assertEquals("'abc'", GenUtility.simpleQuote("abc"));
        assertThrows(UnsupportedOperationException.class, () -> GenUtility.simpleQuote("'"));
    }

    @Test
    void joinPackageName() {
        assertEquals("a.b.c", GenUtility.joinPackageName(null, "a", ".b.c", "."));
        assertEquals("", GenUtility.joinPackageName(null, null));
        assertEquals("", GenUtility.joinPackageName());
    }

    @Test
    void fileOf() {
        assertEquals(
                new File("./a/b/c.txt").getAbsolutePath(),
                GenUtility.fileOf(new File("."), "a.b", "c.txt").getAbsolutePath());
    }

}