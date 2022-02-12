package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.code;

import io.github.codingspeedup.execdoc.blueprint.kb.KbNames;
import io.github.codingspeedup.execdoc.blueprint.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.BpEntity;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.IsOwned;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.code.BpEnumeratedType;
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
public class JdlEnum extends BpEntityCell implements BpEnumeratedType, IsOwned, JdlFieldType {

    @Getter
    @KbFunctor(value = KbNames.VALUE_FUNCTOR, T1 = JdlEnumEntry.class)
    private final List<JdlEnumEntry> value = new ArrayList<>();

    @Getter
    @Setter
    @KbFunctor(KbNames.NAME_FUNCTOR)
    private String name;

    @Getter
    @KbFunctor(KbNames.OWNER_FUNCTOR)
    private BpSheet owner;

    public JdlEnum(Cell cell) {
        super(cell);
    }

    public JdlEnum(String name) {
        this.name = name;
    }

    @Override
    public void setOwner(BpEntity owner) {
        this.owner = (BpSheet) owner;
    }

}
