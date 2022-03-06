package io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.structure;

import io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.structure.BasicSoftwareSystem;
import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.SpringNames;
import io.github.codingspeedup.execdoc.toolbox.utilities.UuidUtility;
import lombok.Getter;
import lombok.Setter;

@KbFunctor
public class SpringSoftwareSystem extends BasicSoftwareSystem {

    @Getter
    @Setter
    @KbFunctor(SpringNames.PACKAGE_NAME_FUNCTOR)
    private String packageName;

    public SpringSoftwareSystem() {
        setKbId(UuidUtility.nextUuid());
    }

}
