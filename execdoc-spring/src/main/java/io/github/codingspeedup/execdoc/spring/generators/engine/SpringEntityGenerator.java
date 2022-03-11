package io.github.codingspeedup.execdoc.spring.generators.engine;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.google.common.base.CaseFormat;
import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.BpType;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.vocabulary.relations.data.BpEntityRelation;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.vocabulary.relations.data.BpManyToOne;
import io.github.codingspeedup.execdoc.spring.generators.SpringGenCtx;
import io.github.codingspeedup.execdoc.spring.generators.spec.SpringEntityFieldSpec;
import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaDocument;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
public class SpringEntityGenerator extends AbstractSpringGenerator {

    public SpringEntityGenerator(SpringGenCtx genCtx) {
        super(genCtx);
    }

    public SpringEntityGenerator(SpringGenCtx genCtx, Map<String, TextFileWrapper> artifacts) {
        super(genCtx, artifacts);
    }

    public void generateSingleKeyArtifacts(SpringEntityFieldSpec fieldSpec) {
        JavaDocument entityJava = (JavaDocument) getArtifacts().computeIfAbsent(
                GenUtility.joinPackageName(fieldSpec.getPackageName(), fieldSpec.getTypeName()),
                key -> maybeGenerateEntityClass(key, fieldSpec));
        ClassOrInterfaceDeclaration entityDeclaration = (ClassOrInterfaceDeclaration) entityJava.getMainTypeDeclaration();
        FieldDeclaration fieldDeclaration = declareField(entityJava, entityDeclaration, fieldSpec);
        fieldDeclaration.addAnnotation("Id");
        if (fieldSpec.getFieldTypeHint().equalsIgnoreCase("long")) {
            String generator = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, fieldSpec.getTypeName()) + "SeqGen";
            fieldDeclaration.addAndGetAnnotation("GeneratedValue")
                    .addPair("strategy", "GenerationType.SEQUENCE")
                    .addPair("generator", new StringLiteralExpr(generator));
            fieldDeclaration.addAndGetAnnotation("SequenceGenerator")
                    .addPair("name", new StringLiteralExpr(generator));
        }
        fieldDeclaration.addAndGetAnnotation("Column")
                .addPair("name", new StringLiteralExpr(fieldSpec.getColumnName()))
                .addPair("nullable", "false");
    }

    public void generateNonKeyArtifacts(SpringEntityFieldSpec fieldSpec) {
        JavaDocument entityJava = (JavaDocument) getArtifacts()
                .get(GenUtility.joinPackageName(fieldSpec.getPackageName(), fieldSpec.getTypeName()));
        ClassOrInterfaceDeclaration entityDeclaration = (ClassOrInterfaceDeclaration) entityJava.getMainTypeDeclaration();
        Optional<FieldDeclaration> declaredField = entityDeclaration.getFieldByName(fieldSpec.getFieldName());
        if (declaredField.isPresent()) {
            log.warn("Filed {} is already declared", fieldSpec.getFieldName());
            return;
        }
        FieldDeclaration fieldDeclaration = declareField(entityJava, entityDeclaration, fieldSpec);
        NormalAnnotationExpr columnAnnotation = fieldDeclaration.addAndGetAnnotation("Column")
                .addPair("name", new StringLiteralExpr(fieldSpec.getColumnName()));
        if (fieldSpec.isRequired()) {
            columnAnnotation.addPair("nullable", "false");
        }
    }

    public void addRelationship(Class<? extends BpEntityRelation> relType, SpringEntityFieldSpec fromField, SpringEntityFieldSpec toField) {
        JavaDocument fromJava = (JavaDocument) getArtifacts().get(GenUtility.joinPackageName(fromField.getPackageName(), fromField.getTypeName()));
        CompilationUnit fromUnit = fromJava.getCompilationUnit();
        ClassOrInterfaceDeclaration fromEntityDeclaration = (ClassOrInterfaceDeclaration) fromJava.getMainTypeDeclaration();
        Optional<FieldDeclaration> declaredField = fromEntityDeclaration.getFieldByName(fromField.getFieldName());
        if (declaredField.isPresent()) {
            log.warn("Filed {} is already declared", fromField.getFieldName());
            return;
        }

        JavaDocument toJava = (JavaDocument) getArtifacts().get(GenUtility.joinPackageName(toField.getPackageName(), toField.getTypeName()));
        CompilationUnit toUnit = toJava.getCompilationUnit();
        ClassOrInterfaceDeclaration toEntityDeclaration = (ClassOrInterfaceDeclaration) toJava.getMainTypeDeclaration();

        if (relType.equals(BpManyToOne.class)) {
            if (!fromField.getPackageName().equals(toField.getPackageName())) {
                fromUnit.addImport(GenUtility.joinPackageName(toField.getPackageName(), toField.getTypeName()));
                toUnit.addImport(GenUtility.joinPackageName(fromField.getPackageName(), fromField.getTypeName()));
            }
            toUnit.addImport(Set.class);
            FieldDeclaration fromFieldDeclaration = fromEntityDeclaration.addField(toField.getTypeName(), fromField.getFieldName(), Modifier.Keyword.PRIVATE);
            fromFieldDeclaration.addAndGetAnnotation("ManyToOne");
            fromFieldDeclaration.addAndGetAnnotation("JoinColumn")
                    .addPair("name", new StringLiteralExpr(fromField.getColumnName()));
            FieldDeclaration toFieldDeclaration = toEntityDeclaration.addField(
                    "Set<" + fromField.getTypeName() + ">",
                    CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, fromField.getTypeName()) + "s",
                    Modifier.Keyword.PRIVATE);
            toFieldDeclaration.addAndGetAnnotation("OneToMany")
                    .addPair("mappedBy", new StringLiteralExpr(fromField.getFieldName()));
        }
    }

    private FieldDeclaration declareField(JavaDocument entityJava, ClassOrInterfaceDeclaration entityDeclaration, SpringEntityFieldSpec fieldSpec) {
        String[] fieldTypeHint = fieldSpec.getFieldTypeHint().split(":");
        String fieldTypeName = fieldTypeHint[0];
        String fieldTypeClass = fieldTypeHint.length == 1 ? null : fieldTypeHint[1];
        boolean largeObject = false;
        if (BpType.class.getName().equals(fieldTypeClass)) {
            fieldTypeClass = null;
            switch (fieldTypeName) {
                case "String":
                case "Long":
                case "Integer":
                case "Float":
                case "Double":
                case "Boolean":
                    break;
                case "BigDecimal":
                    fieldTypeClass = BigDecimal.class.getName();
                    break;
                case "LocalDate":
                    fieldTypeClass = LocalDate.class.getName();
                    break;
                case "ZonedDateTime":
                    fieldTypeClass = ZonedDateTime.class.getName();
                    break;
                case "Instant":
                    fieldTypeClass = Instant.class.getName();
                    break;
                case "Duration":
                    fieldTypeClass = Duration.class.getName();
                    break;
                case "UUID":
                    fieldTypeClass = UUID.class.getName();
                    break;
                case "Blob":
                case "AnyBlob":
                case "ImageBlob":
                    fieldTypeName = "byte[]";
                    largeObject = true;
                    break;
                case "TextBlob":
                    fieldTypeName = "String";
                    largeObject = true;
                    break;
                default:
                    throw new UnsupportedOperationException("Undefined type hint '" + fieldSpec.getFieldTypeHint() + "'");
            }
        } else if (StringUtils.isNotBlank(fieldTypeClass)) {
            throw new UnsupportedOperationException("Unrecognized type hint '" + fieldSpec.getFieldTypeHint() + "'");
        }
        if (StringUtils.isNotBlank(fieldTypeClass)) {
            entityJava.getCompilationUnit().addImport(fieldTypeClass);
        }
        FieldDeclaration fieldDeclaration = entityDeclaration.addField(fieldTypeName, fieldSpec.getFieldName(), Modifier.Keyword.PRIVATE);
        if (largeObject) {
            fieldDeclaration.addAnnotation("Lob");
        }
        return fieldDeclaration;
    }

    private TextFileWrapper maybeGenerateEntityClass(String typeFullName, SpringEntityFieldSpec fieldSpec) {
        Pair<JavaDocument, CompilationUnit> docUnit = GenUtility.maybeCreateJavaClass(
                getProjectSpec().getSrcMainJava(), typeFullName, getGenConfig().isForce());
        CompilationUnit cUnit = docUnit.getRight();
        if (cUnit != null) {
            cUnit.addImport("javax.persistence", false, true);
            cUnit.addImport("lombok.AllArgsConstructor");
            cUnit.addImport("lombok.Builder");
            cUnit.addImport("lombok.Getter");
            cUnit.addImport("lombok.Setter");
            cUnit.addImport("lombok.NoArgsConstructor");

            ClassOrInterfaceDeclaration ciDeclaration = (ClassOrInterfaceDeclaration) docUnit.getLeft().getMainTypeDeclaration();
            SpringDtoGenerator.makeSerializable(ciDeclaration);
            ciDeclaration.addAnnotation("Entity");
            ciDeclaration.addAndGetAnnotation("Table")
                    .addPair("name", new StringLiteralExpr(fieldSpec.getTableName()));
            ciDeclaration.addAnnotation("NoArgsConstructor");
            ciDeclaration.addAnnotation("AllArgsConstructor");
            ciDeclaration.addAnnotation("Builder");
            ciDeclaration.addAnnotation("Getter");
            ciDeclaration.addAnnotation("Setter");
        }
        return docUnit.getLeft();
    }


}
