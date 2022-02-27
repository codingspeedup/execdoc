package io.github.codingspeedup.execdoc.spring.generators.artifacts;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;
import io.github.codingspeedup.execdoc.spring.generators.spec.SpringBootProjectSpec;
import io.github.codingspeedup.execdoc.spring.generators.spec.SpringComponentMethodSpec;
import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaDocument;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RestMethodGenerator {

    private final SpringBootProjectSpec projectSpec;
    private final SpringComponentMethodSpec methodSpec;

    private File restDefFile;

    public RestMethodGenerator(SpringBootProjectSpec projectSpec, SpringComponentMethodSpec methodSpec) {
        this.projectSpec = projectSpec;
        this.methodSpec = methodSpec;

        restDefFile = GenUtility.fileOf(projectSpec.getSrcMainJava(), projectSpec.getRestPackageName(), methodSpec.getTypeSimpleName() + ".java");
    }

    public List<TextFileWrapper> generateArtifacts() {
        JavaDocument restDefJava = generateClassDefinition();


        List<TextFileWrapper> artifacts = new ArrayList<>();
        artifacts.add(restDefJava);
        return artifacts;
    }

    @NotNull
    private JavaDocument generateClassDefinition() {
        JavaDocument restDefJava = new JavaDocument(restDefFile);
        if (!restDefFile.exists()) {
            CompilationUnit cUnit = restDefJava.getCompilationUnit();
            cUnit.setPackageDeclaration(methodSpec.getPackageName());

            cUnit.addImport("lombok.extern.slf4j.Slf4j");
            cUnit.addImport("org.springframework.web.bind.annotation.RequestMapping");
            cUnit.addImport("org.springframework.web.bind.annotation.RestController");

            ClassOrInterfaceDeclaration ciDeclaration = cUnit.addClass(methodSpec.getTypeSimpleName(), Modifier.Keyword.PUBLIC);
            ciDeclaration.addAnnotation("RestController");
            ciDeclaration.addSingleMemberAnnotation("RequestMapping", "\"/" + projectSpec.getRestApiRoot() + "\"");
            ciDeclaration.addAnnotation("Slf4j");

        }
        return restDefJava;
    }

}
