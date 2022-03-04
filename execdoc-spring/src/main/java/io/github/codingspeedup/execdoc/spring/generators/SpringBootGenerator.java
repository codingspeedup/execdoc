package io.github.codingspeedup.execdoc.spring.generators;

import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;
import io.github.codingspeedup.execdoc.kb.Kb;
import io.github.codingspeedup.execdoc.spring.blueprint.SpringBlueprint;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.SpringController;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.SpringControllerMethod;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.structure.SprintSoftwareSystem;
import io.github.codingspeedup.execdoc.spring.blueprint.sheets.ControllerMethodsSheet;
import io.github.codingspeedup.execdoc.spring.generators.engine.SpringRestMethodGenerator;
import io.github.codingspeedup.execdoc.spring.generators.model.SpringBootProject;
import io.github.codingspeedup.execdoc.spring.generators.model.SpringRestMethod;
import io.github.codingspeedup.execdoc.spring.generators.spec.HttpRequestMethod;
import io.github.codingspeedup.execdoc.spring.generators.spec.SpringRestMethodSpec;
import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class SpringBootGenerator {

    private final SpringBootGenCfg genCfg;
    private final Kb kb;

    private final Map<String, TextFileWrapper> artifacts = new LinkedHashMap<>();

    public SpringBootGenerator(SpringBootGenCfg genCfg, SpringBlueprint bp) {
        this.genCfg = genCfg;
        this.kb = bp.compileKb();
    }

    public Map<String, TextFileWrapper> generateArtifacts() {
        SprintSoftwareSystem ss = kb.solveConcept(SprintSoftwareSystem.class);
        SpringBootProject springProject = SpringBootProject.builder()
                .rootFolder(genCfg.getDestinationFolder())
                .rootPackage(ss.getPackageName())
                .build();
        generateRestControllers(springProject);
        return artifacts;
    }

    private void generateRestControllers(SpringBootProject springProject) {
        SpringRestMethodGenerator restGenerator = new SpringRestMethodGenerator(genCfg, springProject, artifacts);
        Set<String> controllerIds = kb.solveConcepts(SpringController.class);
        for (String controllerId : controllerIds) {
            SpringController controller = kb.solveConcept(SpringController.class, controllerId);
            String subPackageName = controller.getOwner().getName().getRight();
            if (BlueprintMaster.DEFAULT_SHEET_NAME.equals(subPackageName)) {
                subPackageName = null;
            }
            for (SpringControllerMethod method : controller.getCodeElement()) {
                SpringRestMethodSpec restMethod = SpringRestMethod.builder()
                        .packageName(GenUtility.joinPackageName(springProject.getRestPackageName(), subPackageName))
                        .typeLemma(controller.getName())
                        .methodLemma(method.getName())
                        .httpMethod(HttpRequestMethod.valueOf(ControllerMethodsSheet.findHttpMethods(method.getAnnotations()).iterator().next()))
                        .build();
                restGenerator.generateArtifacts(restMethod);
            }
        }
    }

}
