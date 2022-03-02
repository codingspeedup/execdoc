package io.github.codingspeedup.execdoc.spring.blueprint;

import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.master.sheets.core.L10nSheet;
import io.github.codingspeedup.execdoc.spring.blueprint.sheets.*;

import java.io.File;


public class SpringMaster extends BlueprintMaster {

    public SpringMaster(File file) {
        super(file);
    }

    @Override
    protected void registerSheets() {
        registerSheet(UtilitySheet.class);
        registerSheet(SpringSystemSheet.class);
        registerSheet(L10nSheet.class);
        registerSheet(DtoSheet.class);
        registerSheet(ControllerMethodsSheet.class);
        registerSheet(ServiceMethodsSheet.class);
        registerSheet(EnumsSheet.class);
        registerSheet(EntitySheet.class);
    }

}
