package io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.structure;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.metamodel.BpNames;
import io.github.codingspeedup.execdoc.kb.KbNames;
import io.github.codingspeedup.execdoc.kb.vocabulary.concepts.KbConcept;
import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@KbFunctor
public class JdlValue implements KbConcept {

    @KbFunctor(value = BpNames.VALUE_FUNCTOR, T1 = String.class)
    private final List<String> value = new ArrayList<>();

    @Getter
    @Setter
    private String kbId;

    public JdlValue(Cell cell) {
        if (cell != null) {
            this.kbId = BpNames.getAtom(cell);
            Row row = cell.getRow();
            for (int colIdx = cell.getColumnIndex(); colIdx <= row.getLastCellNum(); ++colIdx) {
                String v = XlsxUtil.getCellValue(row.getCell(colIdx), String.class);
                if (StringUtils.isBlank(v)) {
                    break;
                }
                value.add(v);
            }
        }
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    public boolean isBlank() {
        return value.stream().noneMatch(StringUtils::isNotBlank);
    }

    public String getValue() {
        return isEmpty() ? null : value.get(0);
    }

    public List<String> getValueList() {
        return value;
    }

}
