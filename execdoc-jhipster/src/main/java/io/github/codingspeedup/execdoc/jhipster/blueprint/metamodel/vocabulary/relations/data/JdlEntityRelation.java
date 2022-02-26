package io.github.codingspeedup.execdoc.jhipster.blueprint.metamodel.vocabulary.relations.data;


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
public abstract class JdlEntityRelation extends RData {

    @SuppressWarnings({"unchecked"})
    public static final Class<? extends JdlEntityRelation>[] ENTITY_RELATIONSHIPS = new Class[]{
            JdlManyToOne.class,
            JdlOneToOne.class,
            JdlOneToMany.class,
            JdlManyToMany.class,
    };

    public JdlEntityRelation(Cell cell, Cell from, Cell to) {
        super(cell, from, to);
    }

    @SneakyThrows
    public static JdlEntityRelation from(SemanticTriple triple) {
        String predicateName = triple.getPredicateName();
        for (Class<? extends JdlEntityRelation> relationshipType : ENTITY_RELATIONSHIPS) {
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