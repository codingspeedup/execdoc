package io.github.codingspeedup.execdoc.spring.generators.engine;

import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;
import io.github.codingspeedup.execdoc.spring.generators.SpringGenCtx;
import io.github.codingspeedup.execdoc.spring.generators.model.HttpRequestMethod;
import io.github.codingspeedup.execdoc.spring.generators.model.SpringBootProject;
import io.github.codingspeedup.execdoc.spring.generators.model.SpringRestMethod;
import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpringRestMethodGeneratorTest {

    @Mock
    SpringGenCtx genCtx;

    @Test
    void generateArtifacts() {
        when(genCtx.getProjectSpec()).thenReturn(SpringBootProject.builder()
                .rootFolder(new File("/Users/iulian/temp/execdoc/demo"))
                .rootPackage("com.example.demo")
                .build());
        SpringRestMethod restMethod = SpringRestMethod.builder()
                .packageName(genCtx.getProjectSpec().getRestPackageName())
                .typeLemma("Foo")
                .methodLemma("bar")
                .httpMethod(HttpRequestMethod.GET)
                .build();
        SpringRestMethodGenerator generator = new SpringRestMethodGenerator(genCtx);
        Map<String, TextFileWrapper> artifacts = generator.generateArtifacts(restMethod);
        for (TextFileWrapper artifact : artifacts.values()) {
            System.out.println(GenUtility.toString(List.of(artifact)));
        }
    }

}