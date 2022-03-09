package io.github.codingspeedup.execdoc.spring.generators.engine;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;
import io.github.codingspeedup.execdoc.spring.generators.SpringGenCtx;
import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaDocument;
import io.github.codingspeedup.execdoc.toolbox.files.Folder;
import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.util.Map;

public class SpringDtoGenerator extends AbstractSpringGenerator {

    public SpringDtoGenerator(SpringGenCtx genCtx) {
        super(genCtx);
    }

    public SpringDtoGenerator(SpringGenCtx genCtx, Map<String, TextFileWrapper> artifacts) {
        super(genCtx, artifacts);
    }

    public static JavaDocument generateRestDto(SpringGenCtx genCtx, Folder folder, String typeFullName) {
        Pair<JavaDocument, CompilationUnit> docUnit = GenUtility.maybeCreateJavaClass(
                folder, typeFullName, genCtx.getConfig().isForce());
        CompilationUnit cUnit = docUnit.getRight();
        if (cUnit != null) {
            cUnit.addImport("lombok.AllArgsConstructor");
            cUnit.addImport("lombok.Builder");
            cUnit.addImport("lombok.Data");
            cUnit.addImport("lombok.NoArgsConstructor");

            ClassOrInterfaceDeclaration ciDeclaration = (ClassOrInterfaceDeclaration) docUnit.getLeft().getMainTypeDeclaration();
            ciDeclaration.addAnnotation("NoArgsConstructor");
            ciDeclaration.addAnnotation("AllArgsConstructor");
            ciDeclaration.addAnnotation("Builder");
            ciDeclaration.addAnnotation("Data");

            makeSerializable(ciDeclaration);
            ciDeclaration.addField(String.class, "someField", Modifier.Keyword.PRIVATE);
        }
        return docUnit.getLeft();
    }

    public static void makeSerializable(ClassOrInterfaceDeclaration ciDeclaration) {
        ciDeclaration.addImplementedType(Serializable.class);
        ciDeclaration.addFieldWithInitializer(long.class, "serialVersionUID", new LongLiteralExpr("1L"),
                Modifier.Keyword.PRIVATE, Modifier.Keyword.STATIC, Modifier.Keyword.FINAL);
    }

}
