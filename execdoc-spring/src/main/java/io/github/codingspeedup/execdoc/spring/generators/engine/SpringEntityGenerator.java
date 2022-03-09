package io.github.codingspeedup.execdoc.spring.generators.engine;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.google.common.base.CaseFormat;
import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;
import io.github.codingspeedup.execdoc.spring.generators.SpringGenCtx;
import io.github.codingspeedup.execdoc.spring.generators.spec.SpringEntityFieldSpec;
import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaDocument;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

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
        FieldDeclaration fieldDeclaration = entityDeclaration.addField(fieldSpec.getFieldTypeHint(), fieldSpec.getFieldName(), Modifier.Keyword.PRIVATE);
        fieldDeclaration.addAnnotation("Id");
        if (fieldSpec.getFieldTypeHint().equalsIgnoreCase("long")) {
            String generator = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, fieldSpec.getTypeName()) + "SeqGen";
            fieldDeclaration.addAndGetAnnotation("GeneratedValue")
                    .addPair("strategy", "GenerationType.SEQUENCE")
                    .addPair("generator", new StringLiteralExpr(generator));
            fieldDeclaration.addAndGetAnnotation("SequenceGenerator")
                    .addPair("name", new StringLiteralExpr(generator));
            fieldDeclaration.addAndGetAnnotation("Column")
                    .addPair("name", new StringLiteralExpr(fieldSpec.getColumnName()));
        }
    }

    public void generateNonKeyArtifacts(SpringEntityFieldSpec fieldSpec) {
    }

    private TextFileWrapper maybeGenerateEntityClass(String typeFullName, SpringEntityFieldSpec fieldSpec) {
        Pair<JavaDocument, CompilationUnit> docUnit = GenUtility.maybeCreateJavaClass(
                getProjectSpec().getSrcMainJava(), typeFullName, getGenConfig().isForce());
        CompilationUnit cUnit = docUnit.getRight();
        if (cUnit != null) {
            cUnit.addImport("javax.persistence", false, true);
            cUnit.addImport("lombok.AllArgsConstructor");
            cUnit.addImport("lombok.Builder");
            cUnit.addImport("lombok.Data");
            cUnit.addImport("lombok.NoArgsConstructor");
            cUnit.addImport("lombok.extern.slf4j.Slf4j");

            ClassOrInterfaceDeclaration ciDeclaration = (ClassOrInterfaceDeclaration) docUnit.getLeft().getMainTypeDeclaration();
            SpringDtoGenerator.makeSerializable(ciDeclaration);
            ciDeclaration.addAnnotation("Entity");
            ciDeclaration.addAndGetAnnotation("Table")
                    .addPair("name", new StringLiteralExpr(fieldSpec.getTableName()));
            ciDeclaration.addAnnotation("NoArgsConstructor");
            ciDeclaration.addAnnotation("AllArgsConstructor");
            ciDeclaration.addAnnotation("Builder");
            ciDeclaration.addAnnotation("Data");
        }
        return docUnit.getLeft();
    }


}
