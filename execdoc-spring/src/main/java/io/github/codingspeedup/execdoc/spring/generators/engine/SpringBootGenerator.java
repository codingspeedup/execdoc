package io.github.codingspeedup.execdoc.spring.generators.engine;

import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;
import io.github.codingspeedup.execdoc.spring.blueprint.SpringBlueprint;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.*;
import io.github.codingspeedup.execdoc.spring.blueprint.sheets.ControllerMethodsSheet;
import io.github.codingspeedup.execdoc.spring.generators.SpringGenConfig;
import io.github.codingspeedup.execdoc.spring.generators.spec.HttpRequestMethod;
import io.github.codingspeedup.execdoc.spring.generators.spec.SpringEnumConstant;
import io.github.codingspeedup.execdoc.spring.generators.spec.SpringRestMethod;
import io.github.codingspeedup.execdoc.spring.generators.spec.SpringServiceMethod;
import io.github.codingspeedup.execdoc.spring.generators.spec.impl.SpringEnumConstantImpl;
import io.github.codingspeedup.execdoc.spring.generators.spec.impl.SpringRestMethodImpl;
import io.github.codingspeedup.execdoc.spring.generators.spec.impl.SpringServiceMethodImpl;
import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;

import java.util.Map;
import java.util.Set;

public class SpringBootGenerator extends AbstractSpringGenerator {

    public SpringBootGenerator(SpringGenConfig genCfg, SpringBlueprint bp) {
        super(genCfg, bp);
    }

    public Map<String, TextFileWrapper> generateArtifacts() {
        if (getGenConfig().isEnums()) {
            generateEnums();
        }
        if (getGenConfig().isRestMethods()) {
            generateRestMethods();
        }
        if (getGenConfig().isServiceMethods()) {
            generateServiceMethods();
        }
        return getArtifacts();
    }

    private void generateEnums() {
        SpringEnumGenerator enumGenerator = new SpringEnumGenerator(getGenCtx(), getArtifacts());
        Set<String> enumIds = getKb().solveConcepts(BpEnum.class);
        for (String enumId : enumIds) {
            BpEnum bpEnum = getKb().solveConcept(BpEnum.class, enumId);
            for (BpEnumEntry entry : bpEnum.getValue()) {
                SpringEnumConstant enumConstant = SpringEnumConstantImpl.builder()
                        .packageName(GenUtility.joinPackageName(getProjectSpec().getFiniteDomainsPackageName(), getSubPackageName(bpEnum)))
                        .typeLemma(bpEnum.getName())
                        .constantName(entry.getName())
                        .constantDescription(entry.getExt())
                        .build();
                enumGenerator.generateArtifacts(enumConstant);
            }
        }
    }

    private void generateServiceMethods() {
        SpringServiceMethodGenerator serviceGenerator = new SpringServiceMethodGenerator(getGenCtx(), getArtifacts());
        Set<String> serviceIds = getKb().solveConcepts(BpService.class);
        for (String serviceId : serviceIds) {
            BpService bpService = getKb().solveConcept(BpService.class, serviceId);
            for (BpServiceMethod method : bpService.getCodeElement()) {
                SpringServiceMethod restMethod = SpringServiceMethodImpl.builder()
                        .packageName(GenUtility.joinPackageName(getProjectSpec().getServicesPackageName(), getSubPackageName(bpService)))
                        .typeLemma(bpService.getName())
                        .methodLemma(method.getName())
                        .build();
                serviceGenerator.generateArtifacts(restMethod);
            }
        }
    }

    private void generateRestMethods() {
        SpringRestMethodGenerator restGenerator = new SpringRestMethodGenerator(getGenCtx(), getArtifacts());
        Set<String> controllerIds = getKb().solveConcepts(BpController.class);
        for (String controllerId : controllerIds) {
            BpController bpController = getKb().solveConcept(BpController.class, controllerId);
            for (BpControllerMethod method : bpController.getCodeElement()) {
                SpringRestMethod restMethod = SpringRestMethodImpl.builder()
                        .packageName(GenUtility.joinPackageName(getProjectSpec().getRestControllerPackageName(), getSubPackageName(bpController)))
                        .typeLemma(bpController.getName())
                        .methodLemma(method.getName())
                        .httpMethod(HttpRequestMethod.valueOf(ControllerMethodsSheet.findHttpMethods(method.getAnnotations()).iterator().next()))
                        .build();
                restGenerator.generateArtifacts(restMethod);
            }
        }
    }

}
