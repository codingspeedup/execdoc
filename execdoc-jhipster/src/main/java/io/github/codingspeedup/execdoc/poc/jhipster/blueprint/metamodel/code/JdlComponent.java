package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.code;

import io.github.codingspeedup.execdoc.blueprint.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.kb.KbNames;
import io.github.codingspeedup.execdoc.blueprint.kb.ontology.BpEntityCell;
import io.github.codingspeedup.execdoc.blueprint.kb.ontology.BpSheet;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.BpEntity;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.IsOwned;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.code.BpClassUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@KbFunctor
public abstract class JdlComponent extends BpEntityCell implements BpClassUnit, IsOwned {

    @Getter
    @Setter
    @KbFunctor(KbNames.NAME_FUNCTOR)
    private String name;

    @Getter
    @KbFunctor(KbNames.OWNER_FUNCTOR)
    private BpSheet owner;

    public JdlComponent(Cell cell) {
        super(cell);
    }

    @Override
    public void setOwner(BpEntity owner) {
        this.owner = (BpSheet) owner;
    }

}
