package io.github.codingspeedup.execdoc.blueprint.kb.individuals;

import io.github.codingspeedup.execdoc.blueprint.kb.BpCell;
import io.github.codingspeedup.execdoc.blueprint.kb.BpNames;
import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.kb.vocabulary.KbConcept;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Map;

@NoArgsConstructor
@KbFunctor
public abstract class BpEntityCell implements KbConcept, BpCell {

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

    public BpEntityCell(Cell cell) {
        assignFrom(cell);
    }

}
