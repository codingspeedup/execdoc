package io.github.codingspeedup.execdoc.jhipster.generators;

import io.github.codingspeedup.execdoc.blueprint.metamodel.BpNames;
import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;
import io.github.codingspeedup.execdoc.kb.Kb;
import io.github.codingspeedup.execdoc.kb.KbNames;
import io.github.codingspeedup.execdoc.kb.KbResult;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.BpDto;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.BpEnum;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.BpEnumEntry;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.data.BpEntity;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.data.BpField;
import io.github.codingspeedup.execdoc.jhipster.blueprint.metamodel.individuals.structure.JdlApplication;
import io.github.codingspeedup.execdoc.jhipster.blueprint.metamodel.individuals.structure.JdlValue;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.vocabulary.concepts.code.BpFieldType;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.vocabulary.relations.data.BpEntityRelation;
import io.github.codingspeedup.execdoc.jhipster.blueprint.sheets.AppSheet;
import io.github.codingspeedup.execdoc.spring.blueprint.sheets.EntitySheet;
import io.github.codingspeedup.execdoc.toolbox.utilities.NumberUtility;
import it.unibo.tuprolog.core.Term;
import it.unibo.tuprolog.core.Var;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * https://www.jhipster.tech/jdl/intro
 */
public class JdlGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdlGenerator.class);

    private static final String INDENT = "  ";
    private static final Var X = Var.of("X");

    private final Kb kb;

    public JdlGenerator(Kb kb) {
        this.kb = kb;
    }

    public String getJdl() {
        StringBuilder jdl = new StringBuilder();
        appendApplications(jdl);
        appendEnums(jdl);
        appendEntities(jdl);
        appendRelationships(jdl);
        return jdl.toString();
    }

    private void appendApplications(StringBuilder jdl) {
        for (String appId : kb.solveConcepts(JdlApplication.class)) {
            JdlApplication ss = kb.solveConcept(JdlApplication.class, appId);
            String baseName;
            JdlValue ssValue = ss.getConfig(AppSheet.BASE_NAME);
            if (ssValue == null || ssValue.isEmpty()) {
                baseName = StringUtils.trimToEmpty(ss.getName()).replaceAll("\\s+", "").toLowerCase(Locale.ROOT);
                if (StringUtils.isBlank(baseName)) {
                    baseName = "app" + appId.toUpperCase(Locale.ROOT);
                }
            } else {
                baseName = ssValue.getValue();
            }
            jdl.append("application {\n");
            jdl.append(INDENT).append("config {\n");
            jdl.append(INDENT).append(INDENT).append(AppSheet.BASE_NAME).append(" ").append(baseName);
            appendAppConfigEntry(jdl, "applicationType", ss, false);
            appendAppConfigEntry(jdl, "authenticationType", ss, false);
            appendAppConfigEntry(jdl, "blueprints", ss, true);
            appendAppConfigEntry(jdl, "buildTool", ss, false);
            appendAppConfigEntry(jdl, "cacheProvider", ss, false);
            appendAppConfigEntry(jdl, "clientFramework", ss, false);
            appendAppConfigEntry(jdl, "clientPackageManager", ss, false);
            appendAppConfigEntry(jdl, "clientTheme", ss, false);
            appendAppConfigEntry(jdl, "clientThemeVariant", ss, false);
            appendAppConfigEntry(jdl, "databaseType", ss, false);
            appendAppConfigEntry(jdl, "devDatabaseType", ss, false);
            appendAppConfigEntry(jdl, "dtoSuffix", ss, false);
            appendAppConfigEntry(jdl, "enableHibernateCache", ss, false);
            appendAppConfigEntry(jdl, "enableSwaggerCodegen", ss, false);
            appendAppConfigEntry(jdl, "enableTranslation", ss, false);
            appendAppConfigEntry(jdl, "entitySuffix", ss, false);
            appendAppConfigEntry(jdl, "jhiPrefix", ss, false);
            appendAppConfigEntry(jdl, "languages", ss, true);
            appendAppConfigEntry(jdl, "messageBroker", ss, false);
            appendAppConfigEntry(jdl, "nativeLanguage", ss, false);
            appendAppConfigEntry(jdl, "packageName", ss, false);
            appendAppConfigEntry(jdl, "prodDatabaseType", ss, false);
            appendAppConfigEntry(jdl, "reactive", ss, false);
            appendAppConfigEntry(jdl, "searchEngine", ss, false);
            appendAppConfigEntry(jdl, "serverPort", ss, false);
            appendAppConfigEntry(jdl, "serviceDiscoveryType", ss, false);
            appendAppConfigEntry(jdl, "skipClient", ss, false);
            appendAppConfigEntry(jdl, "skipServer", ss, false);
            appendAppConfigEntry(jdl, "skipUserManagement", ss, false);
            appendAppConfigEntry(jdl, "testFrameworks", ss, true);
            appendAppConfigEntry(jdl, "websocket", ss, false);
            jdl.append("\n").append(INDENT).append("}\n");
            ssValue = ss.getConfig(AppSheet.ENTITIES);
            if (ssValue != null && !ssValue.isBlank()) {
                jdl.append(INDENT).append(AppSheet.ENTITIES).append(" ");
                jdl.append(ssValue.getValueList().stream().filter(StringUtils::isNotBlank).map(StringUtils::trim).collect(Collectors.joining(", ")));
                jdl.append("\n");
            }
            jdl.append("}\n\n");
        }

    }

    private void appendAppConfigEntry(StringBuilder jdl, String optionName, JdlApplication ss, boolean asList) {
        JdlValue value = ss.getConfig(optionName);
        if (value != null && !value.isBlank()) {
            jdl.append(",\n").append(INDENT).append(INDENT).append(optionName).append(" ");
            if (asList) {
                jdl.append(value.getValueList().stream().collect(Collectors.joining(", ", "[", "]")));
            } else {
                jdl.append(value.getValue());
            }
        }
    }

    private void appendEnums(StringBuilder jdl) {
        Set<String> ids = kb.solveConcepts(BpEnum.class);
        for (String id : ids) {
            BpEnum bpEnum = kb.solveConcept(BpEnum.class, id);
            appendComment(jdl, bpEnum.getDocString(), false);
            jdl.append("\nenum ").append(bpEnum.getName()).append(" {");
            for (BpEnumEntry bpEnumEntry : bpEnum.getValue()) {
                // appendComment(jdl, bpEnumEntry.getDocString(), true);
                jdl.append("\n").append(INDENT).append(bpEnumEntry.getName());
                if (StringUtils.isNotBlank(bpEnumEntry.getExt())) {
                    jdl.append(" (\"").append(bpEnumEntry.getExt()).append("\")");
                }
                jdl.append(",");
            }
            closeBlock(jdl);
        }
    }

    private void appendEntities(StringBuilder jdl) {
        Set<String> ids = kb.solveConcepts(BpEntity.class);
        for (String id : ids) {
            BpEntity bpEntity = kb.solveConcept(BpEntity.class, id);
            appendComment(jdl, bpEntity.getDocString(), false);
            appendEntityAnnotations(jdl, bpEntity.getAnnotations(), false);

            KbResult dtoResult = kb.solve(false,
                    GenUtility.simpleQuote(KbNames.getFunctor(BpDto.class)), "(X),",
                    BpNames.NAME_FUNCTOR, "(X, ", GenUtility.simpleQuote(bpEntity.getName()), ")");
            if (!dtoResult.getYes().isEmpty()) {
                jdl.append("\n@dto(mapstruct)");
            }

            jdl.append("\nentity ").append(bpEntity.getAttributes().get(EntitySheet.ATTRIBUTE_CLASS_NAME)).append(" (").append(bpEntity.getName()).append(") {");
            for (BpField bpField : bpEntity.getItemUnit()) {
                BpFieldType bpFieldType = bpField.getType();
                if (bpFieldType != null) {
                    appendComment(jdl, bpField.getDocString(), true);
                    if (BooleanUtils.toBoolean(bpField.getPrimaryKey())) {
                        jdl.append("\n").append(INDENT).append("@id");
                    }
                    jdl.append("\n").append(INDENT).append("@fieldNameAsDatabaseColumn(").append(bpField.getName()).append(")");
                    jdl.append("\n").append(INDENT).append(bpField.getAttributes().get(EntitySheet.ATTRIBUTE_MEMBER_NAME)).append(" ");
                    appendTypeAndValidation(jdl, bpField, bpFieldType);
                    jdl.append(",");
                } else {
                    LOGGER.warn("Unspecified type for field " + bpField.getKbId());
                }
            }
            closeBlock(jdl);
        }
    }

    private void appendRelationships(StringBuilder jdl) {
        for (Triple<String, String, String> relId : kb.solveRelation(BpEntityRelation.class)) {
            for (String relName : kb.findFunctors(relId.getLeft())) {
                for (Class<? extends BpEntityRelation> relType : BpEntityRelation.ENTITY_RELATIONSHIPS) {
                    if (relName.equals(KbNames.getFunctor(relType))) {
                        BpEntityRelation rel = kb.solveRelation(relType, relId.getLeft());
                        Pair<BpEntity, BpField> from = solveRelationshipEntity(rel.getFrom());
                        Pair<BpEntity, BpField> to = solveRelationshipEntity(rel.getTo());
                        appendRelationship(jdl, rel, from, to);
                    }
                }
            }
        }
    }

    private void appendComment(StringBuilder jdl, String comment, boolean indent) {
        if (StringUtils.isNotBlank(comment)) {
            String bol = indent ? INDENT : "";
            String[] lines = comment.split("\\n");
            jdl.append("\n").append(bol).append("/** ").append(lines[0].trim());
            for (int i = 1; i < lines.length; ++i) {
                jdl.append("\n").append(bol).append(" * ").append(lines[i].trim());
            }
            if (lines.length == 1) {
                jdl.append(" */");
            } else {
                jdl.append("\n").append(bol).append(" */");
            }
        }
    }

    private void appendEntityAnnotations(StringBuilder jdl, Map<String, String> annotations, boolean indent) {
        if (MapUtils.isNotEmpty(annotations)) {
            String bol = indent ? INDENT : "";
            for (Map.Entry<String, String> entry : annotations.entrySet()) {
                if (EntitySheet.ENTITY_OPTIONS.contains(entry.getKey())) {
                    if (EntitySheet.OPT_SKIP_CLIENT.equals(entry.getKey()) && "true".equals(entry.getValue())) {
                        appendAnnotation(jdl, bol, entry);
                    } else if (EntitySheet.OPT_SKIP_SERVER.equals(entry.getKey()) && "true".equals(entry.getValue())) {
                        appendAnnotation(jdl, bol, entry);
                    } else if (EntitySheet.OPT_NO_FLUENT_METHOD.equals(entry.getKey()) && "true".equals(entry.getValue())) {
                        appendAnnotation(jdl, bol, entry);
                    } else if (EntitySheet.OPT_FILTER.equals(entry.getKey()) && "true".equals(entry.getValue())) {
                        appendAnnotation(jdl, bol, entry);
                    } else if (EntitySheet.OPT_READ_ONLY.equals(entry.getKey()) && "true".equals(entry.getValue())) {
                        appendAnnotation(jdl, bol, entry);
                    } else if (EntitySheet.OPT_SERVICE.equals(entry.getKey()) && Arrays.asList("serviceClass", "serviceImpl").contains(entry.getValue())) {
                        appendAnnotation(jdl, bol, entry);
                    } else if (EntitySheet.OPT_PAGINATE.equals(entry.getKey()) && Arrays.asList("pagination", "infinite-scroll").contains(entry.getValue())) {
                        appendAnnotation(jdl, bol, entry);
                    } else if (EntitySheet.OPT_SEARCH.equals(entry.getKey()) && Arrays.asList("elasticsearch").contains(entry.getValue())) {
                        appendAnnotation(jdl, bol, entry);
                    } else if (EntitySheet.OPT_ANGULAR_SUFFIX.equals(entry.getKey()) && StringUtils.isNotBlank(entry.getValue())) {
                        appendAnnotation(jdl, bol, entry);
                    } else if (EntitySheet.OPT_CLIENT_ROOT_FOLDER.equals(entry.getKey()) && StringUtils.isNotBlank(entry.getValue())) {
                        appendAnnotation(jdl, bol, entry);
                    }
                } else {
                    appendAnnotation(jdl, bol, entry);
                }
            }
        }
    }

    private void appendAnnotation(StringBuilder jdl, String bol, Map.Entry<String, String> entry) {
        jdl.append("\n").append(bol).append("@").append(entry.getKey());
        if (StringUtils.isNotBlank(entry.getValue())) {
            jdl.append("(").append(entry.getValue()).append(")");
        }
    }

    private void appendTypeAndValidation(StringBuilder jdl, BpField bpField, BpFieldType bpFieldType) {
        String typeName = bpFieldType.getName();
        jdl.append(typeName);
        if (BooleanUtils.toBoolean(bpField.getRequired())) {
            jdl.append(" required");
        }
        String max = NumberUtility.toStringOrNull(bpField.getMax());
        String min = NumberUtility.toStringOrNull(bpField.getMin());
        String pattern = bpField.getExt();
        switch (typeName) {
            case "String":
                if (StringUtils.isNotBlank(min)) {
                    jdl.append(" minlength(").append(min).append(")");
                }
                if (StringUtils.isNotBlank(max)) {
                    jdl.append(" maxlength(").append(max).append(")");
                }
                if (StringUtils.isNotBlank(pattern)) {
                    jdl.append(" pattern(").append(pattern).append(")");
                }
                break;
            case "Integer":
            case "Long":
            case "BigDecimal":
            case "Float":
            case "Double":
                if (StringUtils.isNotBlank(min)) {
                    jdl.append(" min(").append(min).append(")");
                }
                if (StringUtils.isNotBlank(max)) {
                    jdl.append(" max(").append(max).append(")");
                }
                break;
            case "Blob":
            case "AnyBlob":
            case "ImageBlob":
                if (StringUtils.isNotBlank(min)) {
                    jdl.append(" minbytes(").append(min).append(")");
                }
                if (StringUtils.isNotBlank(max)) {
                    jdl.append(" maxbytes(").append(max).append(")");
                }
                break;
        }
        if (BooleanUtils.toBoolean(bpField.getUnique())) {
            jdl.append(" unique");
        }
    }

    private Pair<BpEntity, BpField> solveRelationshipEntity(String kbId) {
        BpEntity entity = null;
        BpField field = null;
        Set<String> functors = kb.findFunctors(kbId);
        if (functors.contains(KbNames.getFunctor(BpEntity.class))) {
            entity = kb.solveConcept(BpEntity.class, kbId);
        } else if (functors.contains(KbNames.getFunctor(BpField.class))) {
            field = kb.solveConcept(BpField.class, kbId);
            List<Term[]> subst = kb.solveOnce(BpNames.ITEM_UNIT_FUNCTOR, X, Var.anonymous(), kbId).getSubstitutions();
            entity = kb.solveConcept(BpEntity.class, KbResult.asString(subst.get(0)[0]));
        }
        return Pair.of(entity, field);
    }

    private void appendRelationship(StringBuilder jdl, BpEntityRelation rel, Pair<BpEntity, BpField> from, Pair<BpEntity, BpField> to) {
        jdl.append("\nrelationship ").append(rel.getJdlName()).append(" {");
        jdl.append("\n").append(INDENT);
        jdl.append(from.getLeft().getAttributes().get(EntitySheet.ATTRIBUTE_CLASS_NAME));
        if (from.getRight() != null) {
            jdl.append("{").append(from.getRight().getAttributes().get(EntitySheet.ATTRIBUTE_MEMBER_NAME)).append("}");
        }
        jdl.append(" to ");
        jdl.append(to.getLeft().getAttributes().get(EntitySheet.ATTRIBUTE_CLASS_NAME));
        if (to.getRight() != null) {
            jdl.append("{").append(to.getRight().getAttributes().get(EntitySheet.ATTRIBUTE_MEMBER_NAME)).append("}");
        }
        closeBlock(jdl);
    }

    private void closeBlock(StringBuilder jdl) {
        int blockEndIndex = jdl.lastIndexOf("}");
        int commaIndex = jdl.lastIndexOf(",");
        if (blockEndIndex < commaIndex) {
            jdl.replace(commaIndex, commaIndex + 1, "");
        }
        jdl.append("\n}\n");
    }

}
