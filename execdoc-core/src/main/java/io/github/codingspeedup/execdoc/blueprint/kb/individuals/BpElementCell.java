package io.github.codingspeedup.execdoc.blueprint.kb.individuals;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.kb.BpNames;
import io.github.codingspeedup.execdoc.kb.KbNames;
import io.github.codingspeedup.execdoc.kb.vocabulary.KbElement;
import io.github.codingspeedup.execdoc.blueprint.kb.IsNamed;
import io.github.codingspeedup.execdoc.blueprint.master.cells.CellComment;
import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Map;

@NoArgsConstructor
public abstract class BpElementCell implements KbElement {

    @Getter
    @Setter
    private String kbId;

    @Getter
    @Setter
    @KbFunctor(BpNames.DOC_STRING_FUNCTOR)
    private String docString;

    @Getter
    @Setter
    @KbFunctor(value = BpNames.ANNOTATION_FUNCTOR, T1 = String.class, T2 = String.class)
    private Map<String, String> annotations;

    @Getter
    @Setter
    @KbFunctor(value = BpNames.ATTRIBUTE_FUNCTOR, T1 = String.class, T2 = String.class)
    private Map<String, String> attributes;

    public BpElementCell(Cell cell) {
        if (cell != null) {
            this.kbId = KbNames.getAtom(cell);
            String cellCommentString = XlsxUtil.getCellComment(cell);
            if (StringUtils.isNotBlank(cellCommentString)) {
                CellComment cellComment = CellComment.parse(cellCommentString);
                this.docString = cellComment.getDocumentation();
                this.annotations = cellComment.getAnnotations();
                this.attributes = cellComment.getAttributes();
            }
            if (this instanceof IsNamed) {
                ((IsNamed) this).setName(XlsxUtil.getCellValue(cell, String.class));
            }
        }
    }

    @Override
    public String toString() {
        return getKbId();
    }

}
