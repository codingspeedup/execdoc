package io.github.codingspeedup.execdoc.spring.blueprint.metamodel.vocabulary.relations.data;


import io.github.codingspeedup.execdoc.blueprint.master.SemanticTriple;
import io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.relations.data.RData;
import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.kb.KbNames;
import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@KbFunctor
public abstract class BpEntityRelation extends RData {

    @SuppressWarnings({"unchecked"})
    public static final Class<? extends BpEntityRelation>[] ENTITY_RELATIONSHIPS = new Class[]{
            BpManyToOne.class,
            BpOneToOne.class,
            BpOneToMany.class,
            BpManyToMany.class,
    };

    public BpEntityRelation(Cell cell, Cell from, Cell to) {
        super(cell, from, to);
    }

    @SneakyThrows
    public static BpEntityRelation from(SemanticTriple triple) {
        String predicateName = triple.getPredicateName();
        for (Class<? extends BpEntityRelation> relationshipType : ENTITY_RELATIONSHIPS) {
            if (KbNames.getFunctor(relationshipType).equals(predicateName)) {
                Cell relationship = triple.getPredicate();
                Cell from = XlsxUtil.backtraceCellBySimpleFormulaReference(triple.getSubject().get(triple.getSubject().size() - 1));
                Cell to = XlsxUtil.backtraceCellBySimpleFormulaReference(triple.getObject().get(0));
                return relationshipType.getConstructor(Cell.class, Cell.class, Cell.class).newInstance(relationship, from, to);
            }
        }
        return null;
    }

    public abstract String getJdlName();

}
