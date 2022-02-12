package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.code;

import io.github.codingspeedup.execdoc.blueprint.kb.KbNames;
import io.github.codingspeedup.execdoc.blueprint.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.code.BpValue;
import io.github.codingspeedup.execdoc.blueprint.kb.ontology.BpEntityCell;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@KbFunctor
public class JdlEnumEntry extends BpEntityCell implements BpValue {

    @Getter
    @Setter
    @KbFunctor(KbNames.NAME_FUNCTOR)
    private String name;

    @Getter
    @Setter
    @KbFunctor(KbNames.VALUE_FUNCTOR)
    private String ext;

    public JdlEnumEntry(Cell cell) {
        super(cell);
    }

    public JdlEnumEntry(String name, String value) {
        this.name = name;
        this.ext = value;
    }

}
