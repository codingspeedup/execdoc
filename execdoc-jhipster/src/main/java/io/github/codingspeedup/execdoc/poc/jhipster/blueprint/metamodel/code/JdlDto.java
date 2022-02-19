package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.code;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.kb.BpNames;
import io.github.codingspeedup.execdoc.blueprint.kb.individuals.BpEntityCell;
import io.github.codingspeedup.execdoc.blueprint.kb.individuals.BpSheet;
import io.github.codingspeedup.execdoc.kb.vocabulary.KbConcept;
import io.github.codingspeedup.execdoc.blueprint.kb.IsOwned;
import io.github.codingspeedup.execdoc.blueprint.kb.vocabulary.code.BpClassUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@KbFunctor
public class JdlDto extends BpEntityCell implements BpClassUnit, IsOwned {

    @Getter
    @Setter
    @KbFunctor(BpNames.NAME_FUNCTOR)
    private String name;

    @Getter
    @KbFunctor(BpNames.OWNER_FUNCTOR)
    private BpSheet owner;

    public JdlDto(Cell cell) {
        super(cell);
    }

    @Override
    public void setOwner(KbConcept owner) {
        this.owner = (BpSheet) owner;
    }

}
