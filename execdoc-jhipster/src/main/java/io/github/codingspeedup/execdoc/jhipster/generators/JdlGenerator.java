package io.github.codingspeedup.execdoc.jhipster.generators;

import io.github.codingspeedup.execdoc.blueprint.metamodel.BpNames;
import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;
import io.github.codingspeedup.execdoc.kb.Kb;
import io.github.codingspeedup.execdoc.kb.KbNames;
import io.github.codingspeedup.execdoc.kb.KbResult;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.JdlDto;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.JdlEnum;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.JdlEnumEntry;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.data.JdlEntity;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.data.JdlField;
import io.github.codingspeedup.execdoc.jhipster.blueprint.metamodel.individuals.structure.JdlApplication;
import io.github.codingspeedup.execdoc.jhipster.blueprint.metamodel.individuals.structure.JdlValue;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.vocabulary.concepts.code.JdlFieldType;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.vocabulary.relations.data.JdlEntityRelation;
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
        Set<String> ids = kb.solveConcepts(JdlEnum.class);
        for (String id : ids) {
            JdlEnum jdlEnum = kb.solveConcept(JdlEnum.class, id);
            appendComment(jdl, jdlEnum.getDocString(), false);
            jdl.append("\nenum ").append(jdlEnum.getName()).append(" {");
            for (JdlEnumEntry jdlEnumEntry : jdlEnum.getValue()) {
                // appendComment(jdl, jdlEnumEntry.getDocString(), true);
                jdl.append("\n").append(INDENT).append(jdlEnumEntry.getName());
                if (StringUtils.isNotBlank(jdlEnumEntry.getExt())) {
                    jdl.append(" (\"").append(jdlEnumEntry.getExt()).append("\")");
                }
                jdl.append(",");
            }
            closeBlock(jdl);
        }
    }

    private void appendEntities(StringBuilder jdl) {
        Set<String> ids = kb.solveConcepts(JdlEntity.class);
        for (String id : ids) {
            JdlEntity jdlEntity = kb.solveConcept(JdlEntity.class, id);
            appendComment(jdl, jdlEntity.getDocString(), false);
            appendEntityAnnotations(jdl, jdlEntity.getAnnotations(), false);

            KbResult dtoResult = kb.solve(false,
                    GenUtility.simpleQuote(KbNames.getFunctor(JdlDto.class)), "(X),",
                    BpNames.NAME_FUNCTOR, "(X, ", GenUtility.simpleQuote(jdlEntity.getName()), ")");
            if (!dtoResult.getYes().isEmpty()) {
                jdl.append("\n@dto(mapstruct)");
            }

            jdl.append("\nentity ").append(jdlEntity.getAttributes().get(EntitySheet.ATTRIBUTE_CLASS_NAME)).append(" (").append(jdlEntity.getName()).append(") {");
            for (JdlField jdlField : jdlEntity.getItemUnit()) {
                JdlFieldType jdlFieldType = jdlField.getType();
                if (jdlFieldType != null) {
                    appendComment(jdl, jdlField.getDocString(), true);
                    if (BooleanUtils.toBoolean(jdlField.getPrimaryKey())) {
                        jdl.append("\n").append(INDENT).append("@id");
                    }
                    jdl.append("\n").append(INDENT).append("@fieldNameAsDatabaseColumn(").append(jdlField.getName()).append(")");
                    jdl.append("\n").append(INDENT).append(jdlField.getAttributes().get(EntitySheet.ATTRIBUTE_MEMBER_NAME)).append(" ");
                    appendTypeAndValidation(jdl, jdlField, jdlFieldType);
                    jdl.append(",");
                } else {
                    LOGGER.warn("Unspecified type for field " + jdlField.getKbId());
                }
            }
            closeBlock(jdl);
        }
    }

    private void appendRelationships(StringBuilder jdl) {
        for (Triple<String, String, String> relId : kb.solveRelation(JdlEntityRelation.class)) {
            for (String relName : kb.findFunctors(relId.getLeft())) {
                for (Class<? extends JdlEntityRelation> relType : JdlEntityRelation.ENTITY_RELATIONSHIPS) {
                    if (relName.equals(KbNames.getFunctor(relType))) {
                        JdlEntityRelation rel = kb.solveRelation(relType, relId.getLeft());
                        Pair<JdlEntity, JdlField> from = solveRelationshipEntity(rel.getFrom());
                        Pair<JdlEntity, JdlField> to = solveRelationshipEntity(rel.getTo());
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

    private void appendTypeAndValidation(StringBuilder jdl, JdlField jdlField, JdlFieldType jdlFieldType) {
        String typeName = jdlFieldType.getName();
        jdl.append(typeName);
        if (BooleanUtils.toBoolean(jdlField.getRequired())) {
            jdl.append(" required");
        }
        String max = NumberUtility.toStringOrNull(jdlField.getMax());
        String min = NumberUtility.toStringOrNull(jdlField.getMin());
        String pattern = jdlField.getExt();
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
        if (BooleanUtils.toBoolean(jdlField.getUnique())) {
            jdl.append(" unique");
        }
    }

    private Pair<JdlEntity, JdlField> solveRelationshipEntity(String kbId) {
        JdlEntity entity = null;
        JdlField field = null;
        Set<String> functors = kb.findFunctors(kbId);
        if (functors.contains(KbNames.getFunctor(JdlEntity.class))) {
            entity = kb.solveConcept(JdlEntity.class, kbId);
        } else if (functors.contains(KbNames.getFunctor(JdlField.class))) {
            field = kb.solveConcept(JdlField.class, kbId);
            List<Term[]> subst = kb.solveOnce(BpNames.ITEM_UNIT_FUNCTOR, X, Var.anonymous(), kbId).getSubstitutions();
            entity = kb.solveConcept(JdlEntity.class, KbResult.asString(subst.get(0)[0]));
        }
        return Pair.of(entity, field);
    }

    private void appendRelationship(StringBuilder jdl, JdlEntityRelation rel, Pair<JdlEntity, JdlField> from, Pair<JdlEntity, JdlField> to) {
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
