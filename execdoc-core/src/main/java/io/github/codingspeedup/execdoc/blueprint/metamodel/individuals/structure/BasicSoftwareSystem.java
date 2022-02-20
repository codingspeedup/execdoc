package io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.structure;

import io.github.codingspeedup.execdoc.blueprint.metamodel.BpNames;
import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.structure.BpSoftwareSystem;
import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@KbFunctor
public class BasicSoftwareSystem implements BpSoftwareSystem {

    @Getter
    @Setter
    private String kbId;

    @Getter
    @Setter
    @KbFunctor(BpNames.NAME_FUNCTOR)
    private String name;

    public BasicSoftwareSystem(BlueprintMaster master) {
        setKbId(master.getFile().getParentFile().getName());
    }

}