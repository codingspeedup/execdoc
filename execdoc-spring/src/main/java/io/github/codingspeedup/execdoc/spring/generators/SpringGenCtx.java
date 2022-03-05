package io.github.codingspeedup.execdoc.spring.generators;

import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.structure.SprintSoftwareSystem;
import io.github.codingspeedup.execdoc.spring.generators.model.SpringBootProject;
import io.github.codingspeedup.execdoc.spring.generators.spec.SpringBootProjectSpec;
import lombok.Getter;
import lombok.NonNull;

public class SpringGenCtx {

    @Getter
    private final SpringGenConfig config;

    @Getter
    private final SpringKb kb;

    @Getter(lazy = true)
    private final SpringBootProjectSpec projectSpec = buildProjectSpec();

    public SpringGenCtx(SpringGenConfig config, SpringKb kb) {
        this.config = config;
        this.kb = kb;
    }

    private SpringBootProjectSpec buildProjectSpec() {
        SprintSoftwareSystem ss = kb.solveConcept(SprintSoftwareSystem.class);
        return SpringBootProject.builder()
                .rootFolder(config.getDestinationFolder())
                .rootPackage(ss.getPackageName())
                .build();
    }

}
