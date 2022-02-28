package io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@KbFunctor
public class JdlServiceMethod extends JdlComponentMethod {

    public JdlServiceMethod(Cell cell) {
        super(cell);
    }

}
