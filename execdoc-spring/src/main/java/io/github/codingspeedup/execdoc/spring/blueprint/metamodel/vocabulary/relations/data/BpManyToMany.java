package io.github.codingspeedup.execdoc.spring.blueprint.metamodel.vocabulary.relations.data;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@KbFunctor("manyToMany")
public class BpManyToMany extends BpEntityRelation {

    public BpManyToMany(Cell cell, Cell from, Cell to) {
        super(cell, from, to);
    }

    @Override
    public String getJdlName() {
        return "ManyToMany";
    }

}
