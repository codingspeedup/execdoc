package io.github.codingspeedup.execdoc.spring.generators.engine;

import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;
import io.github.codingspeedup.execdoc.spring.blueprint.SpringBlueprint;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.BpController;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.BpControllerMethod;
import io.github.codingspeedup.execdoc.spring.blueprint.sheets.ControllerMethodsSheet;
import io.github.codingspeedup.execdoc.spring.generators.SpringGenConfig;
import io.github.codingspeedup.execdoc.spring.generators.SpringGenCtx;
import io.github.codingspeedup.execdoc.spring.generators.SpringKb;
import io.github.codingspeedup.execdoc.spring.generators.spec.HttpRequestMethod;
import io.github.codingspeedup.execdoc.spring.generators.spec.impl.SpringRestMethodImpl;
import io.github.codingspeedup.execdoc.spring.generators.spec.SpringRestMethod;
import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class SpringBootGenerator {

    private final SpringGenCtx genCtx;
    private final Map<String, TextFileWrapper> artifacts = new LinkedHashMap<>();

    public SpringBootGenerator(SpringGenConfig genCfg, SpringBlueprint bp) {
        genCtx = new SpringGenCtx(genCfg, new SpringKb(bp.compileKb()));
    }

    public Map<String, TextFileWrapper> generateArtifacts() {
        if (genCtx.getConfig().isRestMethods()) {
            generateRestMethods();
        }
        return artifacts;
    }

    private void generateRestMethods() {
        SpringRestMethodGenerator restGenerator = new SpringRestMethodGenerator(genCtx, artifacts);
        Set<String> controllerIds = genCtx.getKb().solveConcepts(BpController.class);
        for (String controllerId : controllerIds) {
            BpController controller = genCtx.getKb().solveConcept(BpController.class, controllerId);
            String subPackageName = controller.getOwner().getName().getRight();
            if (BlueprintMaster.DEFAULT_SHEET_NAME.equals(subPackageName)) {
                subPackageName = null;
            }
            for (BpControllerMethod method : controller.getCodeElement()) {
                SpringRestMethod restMethod = SpringRestMethodImpl.builder()
                        .packageName(GenUtility.joinPackageName(genCtx.getProjectSpec().getRestControllerPackageName(), subPackageName))
                        .typeLemma(controller.getName())
                        .methodLemma(method.getName())
                        .httpMethod(HttpRequestMethod.valueOf(ControllerMethodsSheet.findHttpMethods(method.getAnnotations()).iterator().next()))
                        .build();
                restGenerator.generateArtifacts(restMethod);
            }
        }
    }

}
