package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.code;

import io.github.codingspeedup.execdoc.blueprint.kb.BpNames;
import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.kb.vocabulary.KbConcept;
import io.github.codingspeedup.execdoc.blueprint.kb.IsOwned;
import io.github.codingspeedup.execdoc.blueprint.kb.vocabulary.code.BpEnumeratedType;
import io.github.codingspeedup.execdoc.blueprint.kb.individuals.BpEntityCell;
import io.github.codingspeedup.execdoc.blueprint.kb.individuals.BpSheet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@KbFunctor
public class JdlEnum extends BpEntityCell implements BpEnumeratedType, IsOwned, JdlFieldType {

    @Getter
    @KbFunctor(value = BpNames.VALUE_FUNCTOR, T1 = JdlEnumEntry.class)
    private final List<JdlEnumEntry> value = new ArrayList<>();

    @Getter
    @Setter
    @KbFunctor(BpNames.NAME_FUNCTOR)
    private String name;

    @Getter
    @KbFunctor(BpNames.OWNER_FUNCTOR)
    private BpSheet owner;

    public JdlEnum(Cell cell) {
        super(cell);
    }

    public JdlEnum(String name) {
        this.name = name;
    }

    @Override
    public void setOwner(KbConcept owner) {
        this.owner = (BpSheet) owner;
    }

}
