package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.data;

import io.github.codingspeedup.execdoc.blueprint.kb.KbNames;
import io.github.codingspeedup.execdoc.blueprint.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.BpEntity;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.data.BpRelationalTable;
import io.github.codingspeedup.execdoc.blueprint.kb.ontology.BpEntityCell;
import io.github.codingspeedup.execdoc.blueprint.kb.ontology.BpSheet;
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
    @KbFunctor(value = KbNames.ITEM_UNIT_FUNCTOR, T1 = JdlField.class)
    private final List<JdlField> itemUnit = new ArrayList<>();

    @Getter
    @Setter
    @KbFunctor(KbNames.NAME_FUNCTOR)
    private String name;

    @Getter
    @KbFunctor(KbNames.OWNER_FUNCTOR)
    private BpSheet owner;

    public JdlEntity(Cell cell) {
        super(cell);
    }

    @Override
    public void setOwner(BpEntity owner) {
        this.owner = (BpSheet) owner;
    }

}
