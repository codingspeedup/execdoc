package io.github.codingspeedup.execdoc.spring.generators.engine;

import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;
import io.github.codingspeedup.execdoc.spring.blueprint.SpringBlueprint;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.SpringController;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.SpringControllerMethod;
import io.github.codingspeedup.execdoc.spring.blueprint.sheets.ControllerMethodsSheet;
import io.github.codingspeedup.execdoc.spring.generators.SpringGenConfig;
import io.github.codingspeedup.execdoc.spring.generators.SpringGenCtx;
import io.github.codingspeedup.execdoc.spring.generators.SpringKb;
import io.github.codingspeedup.execdoc.spring.generators.model.HttpRequestMethod;
import io.github.codingspeedup.execdoc.spring.generators.model.SpringRestMethod;
import io.github.codingspeedup.execdoc.spring.generators.spec.SpringRestMethodSpec;
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
        Set<String> controllerIds = genCtx.getKb().solveConcepts(SpringController.class);
        for (String controllerId : controllerIds) {
            SpringController controller = genCtx.getKb().solveConcept(SpringController.class, controllerId);
            String subPackageName = controller.getOwner().getName().getRight();
            if (BlueprintMaster.DEFAULT_SHEET_NAME.equals(subPackageName)) {
                subPackageName = null;
            }
            for (SpringControllerMethod method : controller.getCodeElement()) {
                SpringRestMethodSpec restMethod = SpringRestMethod.builder()
                        .packageName(GenUtility.joinPackageName(genCtx.getProjectSpec().getRestPackageName(), subPackageName))
                        .typeLemma(controller.getName())
                        .methodLemma(method.getName())
                        .httpMethod(HttpRequestMethod.valueOf(ControllerMethodsSheet.findHttpMethods(method.getAnnotations()).iterator().next()))
                        .build();
                restGenerator.generateArtifacts(restMethod);
            }
        }
    }

}
