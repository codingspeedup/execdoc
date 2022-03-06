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
public class BpController extends BpComponent {

    @Getter
    @KbFunctor(value = BpNames.CODE_ELEMENT_FUNCTOR, T1 = BpControllerMethod.class)
    private final List<BpControllerMethod> codeElement = new ArrayList<>();

    public BpController(Cell cell) {
        super(cell);
    }

}
