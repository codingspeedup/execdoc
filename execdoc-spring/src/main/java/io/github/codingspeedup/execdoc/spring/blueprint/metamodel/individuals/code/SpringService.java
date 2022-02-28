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
public class SpringService extends SpringComponent {

    @Getter
    @KbFunctor(value = BpNames.CODE_ELEMENT_FUNCTOR, T1 = SpringServiceMethod.class)
    private final List<SpringServiceMethod> codeElement = new ArrayList<>();

    public SpringService(Cell cell) {
        super(cell);
    }

}
