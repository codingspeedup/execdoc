package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.vocabulary.relations.data;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.vocabulary.relations.data.JdlEntityRelationship;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@KbFunctor("manyToMany")
public class JdlManyToMany extends JdlEntityRelationship {

    public JdlManyToMany(Cell cell, Cell from, Cell to) {
        super(cell, from, to);
    }

    @Override
    public String getJdlName() {
        return "ManyToMany";
    }

}
