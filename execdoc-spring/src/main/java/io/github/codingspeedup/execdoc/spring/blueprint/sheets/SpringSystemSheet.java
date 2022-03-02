package io.github.codingspeedup.execdoc.spring.blueprint.sheets;

import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.master.cells.CellMarkers;
import io.github.codingspeedup.execdoc.blueprint.master.sheets.core.SystemSheet;
import io.github.codingspeedup.execdoc.kb.Kb;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.structure.SprintSoftwareSystem;
import org.apache.poi.ss.usermodel.Sheet;

public class SpringSystemSheet extends SystemSheet {

    public static final String NAME_MARKER = "SYSTEM";
    public static final String TOC_CHAPTER = "Global";

    public static final String ANCHOR_PACKAGE_NAME = CellMarkers.ANCHOR_MARKER + "PackageName";

    public SpringSystemSheet(BlueprintMaster bp, Sheet sheet) {
        super(bp, sheet);
    }

    @Override
    public int initialize() {
        int rowIdx = super.initialize();
        setCellValue(++rowIdx, 1, ANCHOR_PACKAGE_NAME);
        autoSizeColumns(1);
        return rowIdx + 1;
    }

    public String getPackageName() {
        return getAnchorRight(ANCHOR_PACKAGE_NAME);
    }

    @Override
    public void expand(Kb kb) {
        SprintSoftwareSystem ss = new SprintSoftwareSystem();
        ss.setName(getSystemName());
        ss.setPackageName(getPackageName());
        kb.learn(ss);
    }

}
