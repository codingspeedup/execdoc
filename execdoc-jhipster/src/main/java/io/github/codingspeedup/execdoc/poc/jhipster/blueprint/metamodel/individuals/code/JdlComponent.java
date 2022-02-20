package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.individuals.code;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.metamodel.BpNames;
import io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.BpEntityCell;
import io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.BpSheet;
import io.github.codingspeedup.execdoc.kb.vocabulary.concepts.KbConcept;
import io.github.codingspeedup.execdoc.blueprint.metamodel.IsOwned;
import io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.code.BpClassUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@KbFunctor
public abstract class JdlComponent extends BpEntityCell implements BpClassUnit, IsOwned {

    @Getter
    @Setter
    @KbFunctor(BpNames.NAME_FUNCTOR)
    private String name;

    @Getter
    @KbFunctor(BpNames.OWNER_FUNCTOR)
    private BpSheet owner;

    public JdlComponent(Cell cell) {
        super(cell);
    }

    @Override
    public void setOwner(KbConcept owner) {
        this.owner = (BpSheet) owner;
    }

}
