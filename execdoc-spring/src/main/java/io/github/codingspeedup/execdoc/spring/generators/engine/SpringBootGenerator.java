package io.github.codingspeedup.execdoc.spring.generators.engine;

import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;
import io.github.codingspeedup.execdoc.kb.KbNames;
import io.github.codingspeedup.execdoc.spring.blueprint.SpringBlueprint;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.*;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.data.BpEntity;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.data.BpField;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.vocabulary.relations.data.BpEntityRelation;
import io.github.codingspeedup.execdoc.spring.blueprint.sheets.ControllerMethodsSheet;
import io.github.codingspeedup.execdoc.spring.blueprint.sheets.EntitySheet;
import io.github.codingspeedup.execdoc.spring.generators.SpringGenConfig;
import io.github.codingspeedup.execdoc.spring.generators.spec.*;
import io.github.codingspeedup.execdoc.spring.generators.spec.impl.SpringEntityFieldImpl;
import io.github.codingspeedup.execdoc.spring.generators.spec.impl.SpringEnumConstantImpl;
import io.github.codingspeedup.execdoc.spring.generators.spec.impl.SpringRestMethodImpl;
import io.github.codingspeedup.execdoc.spring.generators.spec.impl.SpringServiceMethodImpl;
import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;

public class SpringBootGenerator extends AbstractSpringGenerator {

    public SpringBootGenerator(SpringGenConfig genCfg, SpringBlueprint bp) {
        super(genCfg, bp);
    }

    public Map<String, TextFileWrapper> generateArtifacts() {
        if (getGenConfig().isEnums()) {
            generateEnums();
        }
        if (getGenConfig().isEntities()) {
            generateEntities();
        }
        if (getGenConfig().isRestMethods()) {
            generateRestMethods();
        }
        if (getGenConfig().isServiceMethods()) {
            generateServiceMethods();
        }
        return getArtifacts();
    }

    private void generateEntities() {
        SpringEntityGenerator entityGenerator = new SpringEntityGenerator(getGenCtx(), getArtifacts());
        Set<String> entityIds = getKb().solveConcepts(BpEntity.class);
        for (String entityId : entityIds) {
            BpEntity entity = getKb().solveConcept(BpEntity.class, entityId);
            List<SpringEntityFieldSpec> keyFields = new ArrayList<>();
            List<SpringEntityFieldSpec> nonKeyFields = new ArrayList<>();
            for (BpField field : entity.getItemUnit()) {
                if (field.getType() == null) {
                    continue;
                }
                SpringEntityFieldSpec fieldSpec = buildEntityFieldSpec(entity, field, null);
                if (BooleanUtils.isTrue(field.getPrimaryKey())) {
                    keyFields.add(fieldSpec);
                } else {
                    nonKeyFields.add(fieldSpec);
                }
            }
            if (CollectionUtils.isEmpty(keyFields)) {
                keyFields.add(buildEntityFieldSpec(entity, null, null));
            }
            if (keyFields.size() == 1) {
                entityGenerator.generateSingleKeyArtifacts(keyFields.get(0));
            } else {
                throw new UnsupportedOperationException("Multi-column primary key not supported");
            }
            for (SpringEntityFieldSpec fieldSpec : nonKeyFields) {
                entityGenerator.generateNonKeyArtifacts(fieldSpec);
            }
        }
        for (Triple<String, String, String> relId : getKb().solveRelation(BpEntityRelation.class)) {
            for (String relName : getKb().findFunctors(relId.getLeft())) {
                for (Class<? extends BpEntityRelation> relType : BpEntityRelation.ENTITY_RELATIONSHIPS) {
                    if (relName.equals(KbNames.getFunctor(relType))) {
                        BpEntityRelation rel = getKb().solveRelation(relType, relId.getLeft());
                        Pair<BpEntity, BpField> to = getKb().solveRelationshipEntity(rel.getTo());
                        SpringEntityFieldSpec toSpec = buildEntityFieldSpec(to.getLeft(), to.getRight(), null);
                        Pair<BpEntity, BpField> from = getKb().solveRelationshipEntity(rel.getFrom());
                        SpringEntityFieldSpec fromSpec = buildEntityFieldSpec(from.getLeft(), from.getRight(), toSpec.getFieldTypeHint());
                        entityGenerator.addRelationship(relType, fromSpec, toSpec);
                    }
                }
            }
        }
    }

    private SpringEntityFieldImpl buildEntityFieldSpec(BpEntity entity, BpField field, String altTypeHint) {
        SpringEntityFieldImpl.SpringEntityFieldImplBuilder builder = SpringEntityFieldImpl.builder()
                .packageName(GenUtility.joinPackageName(getProjectSpec().getEntityPackageName(), getSubPackageName(entity)))
                .typeLemma(entity.getAttributes().get(EntitySheet.ATTRIBUTE_CLASS_NAME))
                .tableName(entity.getName());
        if (field == null) {
            builder.fieldTypeHint("Long").fieldName("id").columnName("ID");
        } else {
            if (field.getType() == null) {
                builder.fieldTypeHint(altTypeHint);
            } else {
                builder.fieldTypeHint(field.getType().getName() + ":" + field.getType().getClass().getName());
            }
            builder.fieldName(field.getAttributes().get(EntitySheet.ATTRIBUTE_MEMBER_NAME))
                    .columnName(field.getName().toUpperCase(Locale.ROOT));
        }
        return builder.build();
    }

    private void generateEnums() {
        SpringEnumGenerator enumGenerator = new SpringEnumGenerator(getGenCtx(), getArtifacts());
        Set<String> enumIds = getKb().solveConcepts(BpEnum.class);
        for (String enumId : enumIds) {
            BpEnum bpEnum = getKb().solveConcept(BpEnum.class, enumId);
            for (BpEnumEntry entry : bpEnum.getValue()) {
                SpringEnumConstantSpec enumConstant = SpringEnumConstantImpl.builder()
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
                SpringServiceMethodSpec restMethod = SpringServiceMethodImpl.builder()
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
                SpringRestMethodSpec restMethod = SpringRestMethodImpl.builder()
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
