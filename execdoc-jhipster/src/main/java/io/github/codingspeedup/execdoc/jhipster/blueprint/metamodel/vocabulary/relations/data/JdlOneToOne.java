package io.github.codingspeedup.execdoc.jhipster.blueprint.metamodel.vocabulary.relations.data;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@KbFunctor("oneToOne")
public class JdlOneToOne extends JdlEntityRelation {

    public JdlOneToOne(Cell cell, Cell from, Cell to) {
        super(cell, from, to);
    }

    @Override
    public String getJdlName() {
        return "OneToOne";
    }

}
