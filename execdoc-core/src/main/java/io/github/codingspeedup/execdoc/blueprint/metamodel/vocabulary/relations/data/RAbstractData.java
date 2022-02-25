package io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.relations.data;

import io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.BpRelationCell;
import io.github.codingspeedup.execdoc.kb.KbFunctor;
import org.apache.poi.ss.usermodel.Cell;

@KbFunctor
public class RAbstractData extends BpRelationCell {

    public RAbstractData() {
    }

    public RAbstractData(Cell cell, Cell from, Cell to) {
        super(cell, from, to);
    }

}
