package io.github.codingspeedup.execdoc.spring.generators.artifacts;

import io.github.codingspeedup.execdoc.spring.generators.model.SpringBootProject;
import io.github.codingspeedup.execdoc.spring.generators.model.SpringRestMethod;
import io.github.codingspeedup.execdoc.spring.generators.spec.HttpRequestMethod;
import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

class RestMethodGeneratorTest {

    private SpringBootProject springProject;

    @BeforeEach
    void createProject() {
        springProject = SpringBootProject.builder()
                .rootFolder(new File("/Users/iulian/temp/execdoc/demo"))
                .rootPackage("com.example.demo")
                .build();
    }

    @Test
    void generateArtifacts() {
        SpringRestMethod restMethod = SpringRestMethod.builder()
                .packageName(springProject.getRestPackageName())
                .typeLemma("Foo")
                .methodLemma("bar")
                .httpMethod(HttpRequestMethod.GET)
                .build();
        RestMethodGenerator generator = new RestMethodGenerator(springProject, restMethod);
        for (TextFileWrapper sourceFile : generator.generateArtifacts()) {
            System.out.println(sourceFile.getWrappedFile().getAbsolutePath());
            System.out.println(sourceFile);
        }
    }

}