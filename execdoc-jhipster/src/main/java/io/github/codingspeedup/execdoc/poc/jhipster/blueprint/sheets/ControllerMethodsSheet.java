package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.sheets;

import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.master.sheets.core.AbstractMethodsSheet;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.code.JdlController;
import org.apache.poi.ss.usermodel.Sheet;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.code.JdlControllerMethod;

public class ControllerMethodsSheet extends AbstractMethodsSheet<JdlController, JdlControllerMethod> {

    public static final String NAME_MARKER = "CONTROLLERs" + BlueprintMaster.INSTANTIABLE_SHEET_MARKER;
    public static final String TOC_CHAPTER = "Presentation";

    public ControllerMethodsSheet(BlueprintMaster bp, Sheet sheet) {
        super(bp, sheet, JdlController.class, JdlControllerMethod.class);
    }

}
