package io.github.codingspeedup.execdoc.jhipster.blueprint.sheets;

import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.master.sheets.core.AbstractMethodsSheet;
import io.github.codingspeedup.execdoc.jhipster.blueprint.metamodel.individuals.code.JdlService;
import io.github.codingspeedup.execdoc.jhipster.blueprint.metamodel.individuals.code.JdlServiceMethod;
import org.apache.poi.ss.usermodel.Sheet;

public class ServiceMethodsSheet extends AbstractMethodsSheet<JdlService, JdlServiceMethod> {

    public static final String NAME_MARKER = "SERVICEs" + BlueprintMaster.INSTANTIABLE_SHEET_MARKER;
    public static final String TOC_CHAPTER = "Business";

    public ServiceMethodsSheet(BlueprintMaster bp, Sheet sheet) {
        super(bp, sheet, JdlService.class, JdlServiceMethod.class);
    }

}
