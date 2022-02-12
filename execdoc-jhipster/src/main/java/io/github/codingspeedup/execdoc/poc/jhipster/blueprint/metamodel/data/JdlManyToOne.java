package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.data;

import io.github.codingspeedup.execdoc.blueprint.kb.KbFunctor;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@KbFunctor("manyToOne")
public class JdlManyToOne extends JdlEntityRelationship {

    public JdlManyToOne(Cell cell, Cell from, Cell to) {
        super(cell, from, to);
    }

    @Override
    public String getJdlName() {
        return "ManyToOne";
    }

}
