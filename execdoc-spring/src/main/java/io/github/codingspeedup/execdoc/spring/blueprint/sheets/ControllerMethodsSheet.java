package io.github.codingspeedup.execdoc.spring.blueprint.sheets;

import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.master.sheets.core.AbstractMethodsSheet;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.SpringController;
import org.apache.poi.ss.usermodel.Sheet;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.SpringControllerMethod;

public class ControllerMethodsSheet extends AbstractMethodsSheet<SpringController, SpringControllerMethod> {

    public static final String NAME_MARKER = "CONTROLLERs" + BlueprintMaster.INSTANTIABLE_SHEET_MARKER;
    public static final String TOC_CHAPTER = "Presentation";

    public ControllerMethodsSheet(BlueprintMaster bp, Sheet sheet) {
        super(bp, sheet, SpringController.class, SpringControllerMethod.class);
    }

}
