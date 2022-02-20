package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.vocabulary.relations.data;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.vocabulary.relations.data.JdlEntityRelationship;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@KbFunctor("oneToOne")
public class JdlOneToOne extends JdlEntityRelationship {

    public JdlOneToOne(Cell cell, Cell from, Cell to) {
        super(cell, from, to);
    }

    @Override
    public String getJdlName() {
        return "OneToOne";
    }

}
