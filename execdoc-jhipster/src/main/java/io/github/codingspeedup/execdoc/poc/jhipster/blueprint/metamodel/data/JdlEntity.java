package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.data;

import io.github.codingspeedup.execdoc.blueprint.kb.BpNames;
import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.kb.vocabulary.KbConcept;
import io.github.codingspeedup.execdoc.blueprint.kb.vocabulary.data.BpRelationalTable;
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
