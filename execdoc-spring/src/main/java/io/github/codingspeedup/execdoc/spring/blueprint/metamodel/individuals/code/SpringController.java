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
public class SpringController extends SpringComponent {

    @Getter
    @KbFunctor(value = BpNames.CODE_ELEMENT_FUNCTOR, T1 = SpringControllerMethod.class)
    private final List<SpringControllerMethod> codeElement = new ArrayList<>();

    public SpringController(Cell cell) {
        super(cell);
    }

}
