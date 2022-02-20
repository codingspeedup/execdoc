package io.github.codingspeedup.execdoc.blueprint.metamodel.individuals;

import io.github.codingspeedup.execdoc.blueprint.metamodel.BpCell;
import io.github.codingspeedup.execdoc.blueprint.metamodel.BpNames;
import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.kb.KbNames;
import io.github.codingspeedup.execdoc.kb.vocabulary.BpRelationship;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Map;

@NoArgsConstructor
@KbFunctor
public abstract class BpRelationshipCell extends BpRelationship implements BpCell {

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


    @Getter
    @Setter
    private String from;

    @Getter
    @Setter
    private String to;

    public BpRelationshipCell(Cell cell, Cell from, Cell to) {
        this(cell, KbNames.getAtom(from), KbNames.getAtom(to));
    }

    public BpRelationshipCell(Cell cell, String from, String to) {
        super(null, from, to);
        setKbId(assignFrom(cell));
    }

    @Override
    public String toString() {
        return getKbId();
    }


}
