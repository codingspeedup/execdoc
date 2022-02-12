package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.code;

import io.github.codingspeedup.execdoc.blueprint.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.kb.KbNames;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@KbFunctor
public class JdlService extends JdlComponent {

    @Getter
    @KbFunctor(value = KbNames.CODE_ELEMENT_FUNCTOR, T1 = JdlServiceMethod.class)
    private final List<JdlServiceMethod> codeElement = new ArrayList<>();

    public JdlService(Cell cell) {
        super(cell);
    }

}
