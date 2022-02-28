package io.github.codingspeedup.execdoc.spring.blueprint;

import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.master.sheets.BlueprintSheet;
import org.apache.poi.ss.usermodel.Sheet;

public abstract class SpringSheet extends BlueprintSheet {

    public SpringSheet(BlueprintMaster master, Sheet sheet) {
        super(master, sheet);
    }

}
