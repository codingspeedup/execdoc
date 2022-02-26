package io.github.codingspeedup.execdoc.blueprint.metamodel;

import io.github.codingspeedup.execdoc.blueprint.master.cells.CellComment;
import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Map;

public interface BpCell {

    String getDocString();

    void setDocString(String docString);

    Map<String, String> getAnnotations();

    void setAnnotations(Map<String, String> annotations);

    Map<String, String> getAttributes();

    void setAttributes(Map<String, String> attributes);

    default String assignFrom(Cell cell) {
        String kbId = null;
        if (cell != null) {
            kbId = BpNames.getAtom(cell);
            String cellCommentString = XlsxUtil.getCellComment(cell);
            if (StringUtils.isNotBlank(cellCommentString)) {
                CellComment cellComment = CellComment.parse(cellCommentString);
                setDocString(cellComment.getDocumentation());
                setAnnotations(cellComment.getAnnotations());
                setAttributes(cellComment.getAttributes());
            }
            if (this instanceof IsNamed) {
                ((IsNamed) this).setName(XlsxUtil.getCellValue(cell, String.class));
            }
        }
        return kbId;
    }

}
