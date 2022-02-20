package io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.relations.data;

import io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.BpRelationshipCell;
import io.github.codingspeedup.execdoc.kb.KbFunctor;
import org.apache.poi.ss.usermodel.Cell;

@KbFunctor
public class BpAbstractDataRelationship extends BpRelationshipCell {

    public BpAbstractDataRelationship() {
    }

    public BpAbstractDataRelationship(Cell cell, Cell from, Cell to) {
        super(cell, from, to);
    }

}
