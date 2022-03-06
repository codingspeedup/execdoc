package io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.metamodel.BpNames;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@KbFunctor
public class BpService extends BpComponent {

    @Getter
    @KbFunctor(value = BpNames.CODE_ELEMENT_FUNCTOR, T1 = BpServiceMethod.class)
    private final List<BpServiceMethod> codeElement = new ArrayList<>();

    public BpService(Cell cell) {
        super(cell);
    }

}
