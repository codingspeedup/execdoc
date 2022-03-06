package io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.data;

import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.SpringNames;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.BpEnum;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.BpType;
import io.github.codingspeedup.execdoc.kb.Kb;
import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.metamodel.BpNames;
import io.github.codingspeedup.execdoc.kb.KbResult;
import io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.BpConceptCell;
import io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.code.CItemUnit;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.vocabulary.concepts.code.BpFieldType;
import it.unibo.tuprolog.core.Term;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@KbFunctor
public class BpField extends BpConceptCell implements CItemUnit {

    @Getter
    @Setter
    @KbFunctor(BpNames.NAME_FUNCTOR)
    private String name;

    @Getter
    @Setter
    private BpFieldType type;

    @Getter
    @Setter
    @KbFunctor(BpNames.KEY_FUNCTOR)
    private Boolean primaryKey;

    @Getter
    @Setter
    @KbFunctor(SpringNames.REQUIRED_FUNCTOR)
    private Boolean required;

    @Getter
    @Setter
    @KbFunctor(SpringNames.MIN_FUNCTOR)
    private BigDecimal min;

    @Getter
    @Setter
    @KbFunctor(SpringNames.MAX_FUNCTOR)
    private BigDecimal max;

    @Getter
    @Setter
    @KbFunctor(SpringNames.PATTERN_FUNCTOR)
    private String ext;

    @Getter
    @Setter
    @KbFunctor(SpringNames.UNIQUE_FUNCTOR)
    private Boolean unique;

    public BpField(Cell cell) {
        super(cell);
    }

    @Override
    public void kbStore(Kb kb) {
        super.kbStore(kb);
        if (type != null) {
            kb.learn(BpNames.TYPE_FUNCTOR, getKbId(), type.getKbId());
        }
    }

    @Override
    public void kbRetrieve(Kb kb) {
        super.kbRetrieve(kb);
        KbResult result = kb.solveOnce(BpNames.TYPE_FUNCTOR, getKbId(), "X");
        List<Term[]> substitutions = result.getSubstitutions();
        if (CollectionUtils.isNotEmpty(substitutions)) {
            String typeId = KbResult.asString(substitutions.get(0)[0]);
            if (typeId.startsWith(BpType.PREFIX)) {
                type = new BpType(typeId.substring(BpType.PREFIX.length()));
            } else {
                type = kb.solveConcept(BpEnum.class, typeId);
            }
        }
    }

}
