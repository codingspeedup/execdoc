package io.github.codingspeedup.execdoc.jhipster.blueprint.metamodel.individuals.code;

import io.github.codingspeedup.execdoc.blueprint.metamodel.BpNames;
import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.kb.vocabulary.concepts.KbConcept;
import io.github.codingspeedup.execdoc.blueprint.metamodel.IsOwned;
import io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.code.CEnumeratedType;
import io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.BpConceptCell;
import io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.BpSheet;
import io.github.codingspeedup.execdoc.jhipster.blueprint.metamodel.vocabulary.concepts.code.JdlFieldType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@KbFunctor
public class JdlEnum extends BpConceptCell implements CEnumeratedType, IsOwned, JdlFieldType {

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
