package io.github.codingspeedup.execdoc.space;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class SpaceTest {

    @Test
    void of() throws IOException {
        var file = new File("./src/test/resources/space-samples/books.xml");
        try (var ins = new FileInputStream(file)) {
            var space = Space.of(ins);
            System.out.println(space);
        }
    }

}