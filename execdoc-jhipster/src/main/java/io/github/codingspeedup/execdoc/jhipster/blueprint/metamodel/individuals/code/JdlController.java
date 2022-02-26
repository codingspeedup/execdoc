package io.github.codingspeedup.execdoc.jhipster.blueprint.metamodel.individuals.code;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.metamodel.BpNames;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@KbFunctor
public class JdlController extends JdlComponent {

    @Getter
    @KbFunctor(value = BpNames.CODE_ELEMENT_FUNCTOR, T1 = JdlControllerMethod.class)
    private final List<JdlControllerMethod> codeElement = new ArrayList<>();

    public JdlController(Cell cell) {
        super(cell);
    }

}
