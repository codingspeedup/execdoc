package io.github.codingspeedup.execdoc.blueprint.kb.individuals;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.kb.vocabulary.KbConcept;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@KbFunctor
public abstract class BpEntityCell extends BpElementCell implements KbConcept {

    public BpEntityCell(Cell cell) {
        super(cell);
    }

}
