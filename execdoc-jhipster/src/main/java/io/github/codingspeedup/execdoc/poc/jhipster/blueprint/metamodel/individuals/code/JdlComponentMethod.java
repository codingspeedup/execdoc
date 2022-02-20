package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.individuals.code;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.metamodel.BpNames;
import io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.BpEntityCell;
import io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.code.BpMethodUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@KbFunctor
public abstract class JdlComponentMethod extends BpEntityCell implements BpMethodUnit {

    @Getter
    @Setter
    @KbFunctor(BpNames.NAME_FUNCTOR)
    private String name;

    public JdlComponentMethod(Cell cell) {
        super(cell);
    }

}
