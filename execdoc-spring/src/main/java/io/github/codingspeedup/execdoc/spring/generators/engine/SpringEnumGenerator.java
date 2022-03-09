package io.github.codingspeedup.execdoc.spring.generators.engine;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;
import io.github.codingspeedup.execdoc.spring.generators.SpringGenCtx;
import io.github.codingspeedup.execdoc.spring.generators.spec.SpringEnumConstantSpec;
import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaDocument;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

public class SpringEnumGenerator extends AbstractSpringGenerator {

    public SpringEnumGenerator(SpringGenCtx genCtx) {
        super(genCtx);
    }

    public SpringEnumGenerator(SpringGenCtx genCtx, Map<String, TextFileWrapper> artifacts) {
        super(genCtx, artifacts);
    }

    public void generateArtifacts(SpringEnumConstantSpec entry) {
        JavaDocument enumJava = (JavaDocument) getArtifacts().computeIfAbsent(
                GenUtility.joinPackageName(entry.getPackageName(), entry.getTypeName()),
                key -> maybeGenerateEnumClass(key));
        EnumDeclaration enumDeclaration = (EnumDeclaration) enumJava.getMainTypeDeclaration();
        for (EnumConstantDeclaration constantDeclaration : enumDeclaration.getEntries()) {
            if (constantDeclaration.getNameAsString().equals(entry.getConstantName())) {
                return;
            }
        }
        EnumConstantDeclaration constantDeclaration = enumDeclaration.addEnumConstant(entry.getConstantName());
        constantDeclaration.addArgument(new StringLiteralExpr(entry.getConstantDescription()));
    }

    private TextFileWrapper maybeGenerateEnumClass(String typeFullName) {
        Pair<JavaDocument, CompilationUnit> docUnit = GenUtility.maybeCreateJavaEnum(
                getProjectSpec().getSrcMainJava(), typeFullName, getGenConfig().isForce());
        CompilationUnit cUnit = docUnit.getRight();
        if (cUnit != null) {
            EnumDeclaration enumDeclaration = (EnumDeclaration) docUnit.getLeft().getMainTypeDeclaration();
            enumDeclaration.addField("String", "description", Modifier.Keyword.PRIVATE, Modifier.Keyword.FINAL);
            enumDeclaration.addConstructor()
                    .addParameter("String", "description")
                    .getBody()
                    .addStatement("this.description = description;");
            enumDeclaration.addMethod("description", Modifier.Keyword.PUBLIC)
                    .setType("String")
                    .getBody().orElseThrow(() -> new UnsupportedOperationException())
                    .addStatement("return description;");
        }
        return docUnit.getLeft();
    }

}
