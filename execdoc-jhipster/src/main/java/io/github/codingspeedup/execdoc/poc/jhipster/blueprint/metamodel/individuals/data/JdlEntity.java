package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.individuals.data;

import io.github.codingspeedup.execdoc.blueprint.metamodel.BpNames;
import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.kb.vocabulary.concepts.KbConcept;
import io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.data.BpRelationalTable;
import io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.BpEntityCell;
import io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.BpSheet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@KbFunctor
public class JdlEntity extends BpEntityCell implements BpRelationalTable {

    @Getter
    @KbFunctor(value = BpNames.ITEM_UNIT_FUNCTOR, T1 = JdlField.class)
    private final List<JdlField> itemUnit = new ArrayList<>();

    @Getter
    @Setter
    @KbFunctor(BpNames.NAME_FUNCTOR)
    private String name;

    @Getter
    @KbFunctor(BpNames.OWNER_FUNCTOR)
    private BpSheet owner;

    public JdlEntity(Cell cell) {
        super(cell);
    }

    @Override
    public void setOwner(KbConcept owner) {
        this.owner = (BpSheet) owner;
    }

}
