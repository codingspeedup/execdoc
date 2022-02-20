package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.individuals.code;

import io.github.codingspeedup.execdoc.blueprint.metamodel.BpNames;
import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.kb.vocabulary.concepts.KbConcept;
import io.github.codingspeedup.execdoc.blueprint.metamodel.IsOwned;
import io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.code.BpEnumeratedType;
import io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.BpEntityCell;
import io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.BpSheet;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.vocabulary.concepts.code.JdlFieldType;
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
