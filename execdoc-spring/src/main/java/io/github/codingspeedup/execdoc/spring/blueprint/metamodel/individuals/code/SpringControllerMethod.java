package io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@KbFunctor
public class SpringControllerMethod extends SpringComponentMethod {

    public SpringControllerMethod(Cell cell) {
        super(cell);
    }

}
