package io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.metamodel.BpNames;
import io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.BpConceptCell;
import io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.code.CMethodUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@KbFunctor
public abstract class BpComponentMethod extends BpConceptCell implements CMethodUnit {

    @Getter
    @Setter
    @KbFunctor(BpNames.NAME_FUNCTOR)
    private String name;

    public BpComponentMethod(Cell cell) {
        super(cell);
    }

}
