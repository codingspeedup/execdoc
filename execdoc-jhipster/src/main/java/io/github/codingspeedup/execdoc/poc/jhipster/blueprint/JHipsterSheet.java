package io.github.codingspeedup.execdoc.poc.jhipster.blueprint;

import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.master.sheets.BlueprintSheet;
import org.apache.poi.ss.usermodel.Sheet;

public abstract class JHipsterSheet extends BlueprintSheet {

    public JHipsterSheet(BlueprintMaster master, Sheet sheet) {
        super(master, sheet);
    }

}
