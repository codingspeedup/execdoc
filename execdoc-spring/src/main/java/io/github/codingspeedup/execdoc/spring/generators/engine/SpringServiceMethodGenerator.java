package io.github.codingspeedup.execdoc.spring.generators.engine;

import io.github.codingspeedup.execdoc.spring.generators.SpringGenCtx;
import io.github.codingspeedup.execdoc.spring.generators.spec.SpringServiceMethod;
import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;

import java.util.LinkedHashMap;
import java.util.Map;

public class SpringServiceMethodGenerator {

    private final SpringGenCtx genCtx;
    private final Map<String, TextFileWrapper> artifacts;

    public SpringServiceMethodGenerator(SpringGenCtx genCtx) {
        this(genCtx, null);
    }

    public SpringServiceMethodGenerator(SpringGenCtx genCtx, Map<String, TextFileWrapper> artifacts) {
        this.genCtx = genCtx;
        this.artifacts = artifacts == null ? new LinkedHashMap<>() : artifacts;
    }

    public Map<String, TextFileWrapper> generateArtifacts(SpringServiceMethod methodSpec) {
        return artifacts;
    }


}
