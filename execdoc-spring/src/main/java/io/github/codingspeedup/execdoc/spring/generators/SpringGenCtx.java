package io.github.codingspeedup.execdoc.spring.generators;

import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.structure.SpringSoftwareSystem;
import io.github.codingspeedup.execdoc.spring.generators.spec.impl.SpringBootProjectImpl;
import io.github.codingspeedup.execdoc.spring.generators.spec.SpringBootProjectSpec;
import lombok.Getter;

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
        SpringSoftwareSystem ss = kb.solveConcept(SpringSoftwareSystem.class);
        return SpringBootProjectImpl.builder()
                .rootFolder(config.getDestinationFolder())
                .rootPackage(ss.getPackageName())
                .build();
    }

}
