package io.github.codingspeedup.execdoc.spring.generators.engine;

import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.metamodel.IsOwned;
import io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.BpSheet;
import io.github.codingspeedup.execdoc.spring.blueprint.SpringBlueprint;
import io.github.codingspeedup.execdoc.spring.generators.SpringGenConfig;
import io.github.codingspeedup.execdoc.spring.generators.SpringGenCtx;
import io.github.codingspeedup.execdoc.spring.generators.SpringKb;
import io.github.codingspeedup.execdoc.spring.generators.spec.SpringBootProjectSpec;
import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

public class AbstractSpringGenerator {

    @Getter
    private final SpringGenCtx genCtx;
    @Getter
    private final Map<String, TextFileWrapper> artifacts;

    public AbstractSpringGenerator(SpringGenConfig genCfg, SpringBlueprint bp) {
        genCtx = new SpringGenCtx(genCfg, new SpringKb(bp.compileKb()));
        artifacts = new LinkedHashMap<>();
    }

    public AbstractSpringGenerator(SpringGenCtx genCtx) {
        this(genCtx, null);
    }

    public AbstractSpringGenerator(SpringGenCtx genCtx, Map<String, TextFileWrapper> artifacts) {
        this.genCtx = genCtx;
        this.artifacts = artifacts == null ? new LinkedHashMap<>() : artifacts;
    }

    public SpringGenConfig getGenConfig() {
        return genCtx.getConfig();
    }

    public SpringKb getKb() {
        return genCtx.getKb();
    }

    public SpringBootProjectSpec getProjectSpec() {
        return genCtx.getProjectSpec();
    }

    public String getSubPackageName(IsOwned bpComponent) {
        String subPackageName = ((BpSheet) bpComponent.getOwner()).getName().getRight();
        if (BlueprintMaster.DEFAULT_SHEET_NAME.equals(subPackageName)) {
            subPackageName = null;
        }
        return subPackageName;
    }

}
