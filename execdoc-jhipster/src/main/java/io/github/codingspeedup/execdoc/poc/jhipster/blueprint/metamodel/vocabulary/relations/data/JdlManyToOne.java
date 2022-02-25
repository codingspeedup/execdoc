package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.vocabulary.relations.data;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@KbFunctor("manyToOne")
public class JdlManyToOne extends JdlEntityRelation {

    public JdlManyToOne(Cell cell, Cell from, Cell to) {
        super(cell, from, to);
    }

    @Override
    public String getJdlName() {
        return "ManyToOne";
    }

}
