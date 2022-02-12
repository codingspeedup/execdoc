package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.data;

import io.github.codingspeedup.execdoc.blueprint.kb.BpKb;
import io.github.codingspeedup.execdoc.blueprint.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.kb.KbNames;
import io.github.codingspeedup.execdoc.blueprint.kb.KbResult;
import io.github.codingspeedup.execdoc.blueprint.kb.ontology.BpEntityCell;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.code.BpItemUnit;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.JdlNames;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.code.JdlEnum;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.code.JdlFieldType;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.code.JdlType;
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
public class JdlField extends BpEntityCell implements BpItemUnit {

    @Getter
    @Setter
    @KbFunctor(KbNames.NAME_FUNCTOR)
    private String name;

    @Getter
    @Setter
    private JdlFieldType type;

    @Getter
    @Setter
    @KbFunctor(KbNames.KEY_FUNCTOR)
    private Boolean primaryKey;

    @Getter
    @Setter
    @KbFunctor(JdlNames.REQUIRED_FUNCTOR)
    private Boolean required;

    @Getter
    @Setter
    @KbFunctor(JdlNames.MIN_FUNCTOR)
    private BigDecimal min;

    @Getter
    @Setter
    @KbFunctor(JdlNames.MAX_FUNCTOR)
    private BigDecimal max;

    @Getter
    @Setter
    @KbFunctor(JdlNames.PATTERN_FUNCTOR)
    private String ext;

    @Getter
    @Setter
    @KbFunctor(JdlNames.UNIQUE_FUNCTOR)
    private Boolean unique;

    public JdlField(Cell cell) {
        super(cell);
    }

    @Override
    public void kbStore(BpKb bpKb) {
        super.kbStore(bpKb);
        if (type != null) {
            bpKb.learn(KbNames.TYPE_FUNCTOR, getKbId(), type.getKbId());
        }
    }

    @Override
    public void kbRetrieve(BpKb bpKb) {
        super.kbRetrieve(bpKb);
        KbResult result = bpKb.solveOnce(KbNames.TYPE_FUNCTOR, getKbId(), "X");
        List<Term[]> substitutions = result.getSubstitutions();
        if (CollectionUtils.isNotEmpty(substitutions)) {
            String typeId = KbResult.asString(substitutions.get(0)[0]);
            if (typeId.startsWith(JdlType.PREFIX)) {
                type = new JdlType(typeId.substring(JdlType.PREFIX.length()));
            } else {
                type = bpKb.solveEntity(JdlEnum.class, typeId);
            }
        }
    }

}
