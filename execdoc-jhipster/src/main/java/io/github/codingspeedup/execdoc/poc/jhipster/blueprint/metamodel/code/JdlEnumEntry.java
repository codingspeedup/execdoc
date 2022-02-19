package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.code;

import io.github.codingspeedup.execdoc.blueprint.kb.BpNames;
import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.kb.vocabulary.code.BpValue;
import io.github.codingspeedup.execdoc.blueprint.kb.individuals.BpEntityCell;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@KbFunctor
public class JdlEnumEntry extends BpEntityCell implements BpValue {

    @Getter
    @Setter
    @KbFunctor(BpNames.NAME_FUNCTOR)
    private String name;

    @Getter
    @Setter
    @KbFunctor(BpNames.VALUE_FUNCTOR)
    private String ext;

    public JdlEnumEntry(Cell cell) {
        super(cell);
    }

    public JdlEnumEntry(String name, String value) {
        this.name = name;
        this.ext = value;
    }

}
