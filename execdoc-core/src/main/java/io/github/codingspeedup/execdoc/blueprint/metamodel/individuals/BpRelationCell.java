package io.github.codingspeedup.execdoc.blueprint.metamodel.individuals;

import io.github.codingspeedup.execdoc.blueprint.metamodel.BpCell;
import io.github.codingspeedup.execdoc.blueprint.metamodel.BpNames;
import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.kb.KbNames;
import io.github.codingspeedup.execdoc.kb.vocabulary.relations.KbRelation2;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Map;

@NoArgsConstructor
@KbFunctor
public abstract class BpRelationCell extends KbRelation2 implements BpCell {

    @Getter
    @Setter
    private String kbId;

    @Getter
    @Setter
    @KbFunctor(BpNames.DOC_STRING_FUNCTOR)
    private String docString;

    @Getter
    @Setter
    @KbFunctor(value = BpNames.ANNOTATION_FUNCTOR, T1 = String.class, T2 = String.class)
    private Map<String, String> annotations;

    @Getter
    @Setter
    @KbFunctor(value = BpNames.ATTRIBUTE_FUNCTOR, T1 = String.class, T2 = String.class)
    private Map<String, String> attributes;

    public BpRelationCell(Cell cell, Cell from, Cell to) {
        this(cell, KbNames.getAtom(from), KbNames.getAtom(to));
    }

    public BpRelationCell(Cell cell, String from, String to) {
        super(null, from, to);
        setKbId(assignFrom(cell));
    }

    @Override
    public String toString() {
        return getKbId();
    }


}
