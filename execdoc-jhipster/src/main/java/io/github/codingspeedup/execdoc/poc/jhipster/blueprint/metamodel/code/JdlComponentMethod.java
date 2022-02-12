package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.code;

import io.github.codingspeedup.execdoc.blueprint.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.kb.KbNames;
import io.github.codingspeedup.execdoc.blueprint.kb.ontology.BpEntityCell;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.code.BpMethodUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@KbFunctor
public abstract class JdlComponentMethod extends BpEntityCell implements BpMethodUnit {

    @Getter
    @Setter
    @KbFunctor(KbNames.NAME_FUNCTOR)
    private String name;

    public JdlComponentMethod(Cell cell) {
        super(cell);
    }

}
