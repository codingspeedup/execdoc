package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.code;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.kb.BpNames;
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
