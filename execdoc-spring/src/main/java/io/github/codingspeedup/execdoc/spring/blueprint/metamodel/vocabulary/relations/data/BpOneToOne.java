package io.github.codingspeedup.execdoc.spring.blueprint.metamodel.vocabulary.relations.data;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@KbFunctor("oneToOne")
public class BpOneToOne extends BpEntityRelation {

    public BpOneToOne(Cell cell, Cell from, Cell to) {
        super(cell, from, to);
    }

    @Override
    public String getJdlName() {
        return "OneToOne";
    }

}
