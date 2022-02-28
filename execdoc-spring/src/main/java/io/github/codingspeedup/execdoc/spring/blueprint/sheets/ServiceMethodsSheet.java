package io.github.codingspeedup.execdoc.spring.blueprint.sheets;

import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.master.sheets.core.AbstractMethodsSheet;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.SpringService;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.SpringServiceMethod;
import org.apache.poi.ss.usermodel.Sheet;

public class ServiceMethodsSheet extends AbstractMethodsSheet<SpringService, SpringServiceMethod> {

    public static final String NAME_MARKER = "SERVICEs" + BlueprintMaster.INSTANTIABLE_SHEET_MARKER;
    public static final String TOC_CHAPTER = "Business";

    public ServiceMethodsSheet(BlueprintMaster bp, Sheet sheet) {
        super(bp, sheet, SpringService.class, SpringServiceMethod.class);
    }

}
