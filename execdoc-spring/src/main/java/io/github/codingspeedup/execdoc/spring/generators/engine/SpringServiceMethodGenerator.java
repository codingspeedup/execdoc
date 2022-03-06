package io.github.codingspeedup.execdoc.spring.generators.engine;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;
import io.github.codingspeedup.execdoc.spring.generators.SpringGenCtx;
import io.github.codingspeedup.execdoc.spring.generators.spec.SpringServiceMethod;
import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaDocument;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SpringServiceMethodGenerator {

    private final SpringGenCtx genCtx;
    private final Map<String, TextFileWrapper> artifacts;

    public SpringServiceMethodGenerator(SpringGenCtx genCtx) {
        this(genCtx, null);
    }

    public SpringServiceMethodGenerator(SpringGenCtx genCtx, Map<String, TextFileWrapper> artifacts) {
        this.genCtx = genCtx;
        this.artifacts = artifacts == null ? new LinkedHashMap<>() : artifacts;
    }

    public Map<String, TextFileWrapper> generateArtifacts(SpringServiceMethod methodSpec) {
        JavaDocument interfaceJava = (JavaDocument) artifacts.computeIfAbsent(
                GenUtility.joinPackageName(methodSpec.getPackageName(), methodSpec.getTypeName()),
                key -> maybeGenerateServiceInterface(key));
        ClassOrInterfaceDeclaration interfaceDeclaration = (ClassOrInterfaceDeclaration) interfaceJava.getMainTypeDeclaration();
        List<MethodDeclaration> methodDeclarations = interfaceDeclaration.getMethodsByName(methodSpec.getMethodName());
        if (CollectionUtils.isEmpty(methodDeclarations)) {
            JavaDocument serviceJava = (JavaDocument) artifacts.computeIfAbsent(
                    GenUtility.joinPackageName(methodSpec.getImplementationPackageName(), methodSpec.getImplementingTypeName()),
                    key -> maybeGenerateServiceImplementation(key, interfaceJava));

            JavaDocument inputDtoJava = (JavaDocument) artifacts.computeIfAbsent(
                    GenUtility.joinPackageName(methodSpec.getDtoPackageName(), methodSpec.getDtoInputTypeName()),
                    key -> maybeGenerateArgumentsDtoClass(key));
            JavaDocument outputDtoJava = (JavaDocument) artifacts.computeIfAbsent(
                    GenUtility.joinPackageName(methodSpec.getDtoPackageName(), methodSpec.getDtoOutputTypeName()),
                    key -> maybeGenerateResultDtoClass(key));

            addInterfaceMethod(interfaceJava, methodSpec, inputDtoJava, outputDtoJava);
            addImplementedMethod(serviceJava, methodSpec, inputDtoJava, outputDtoJava);


        }
        return artifacts;
    }

    private void addImplementedMethod(JavaDocument serviceJava, SpringServiceMethod methodSpec, JavaDocument inputDtoJava, JavaDocument outputDtoJava) {
        CompilationUnit serviceUnit = serviceJava.getCompilationUnit();
        serviceUnit.addImport(GenUtility.joinPackageName(inputDtoJava.getPackageName(), inputDtoJava.getMainTypeDeclaration().getNameAsString()));
        serviceUnit.addImport(GenUtility.joinPackageName(outputDtoJava.getPackageName(), outputDtoJava.getMainTypeDeclaration().getNameAsString()));

        ClassOrInterfaceDeclaration serviceDeclaration = (ClassOrInterfaceDeclaration) serviceJava.getMainTypeDeclaration();
        MethodDeclaration methodDeclaration =  serviceDeclaration.addMethod(methodSpec.getMethodName(), Modifier.Keyword.PUBLIC);
        methodDeclaration.addParameter(inputDtoJava.getMainTypeDeclaration().getNameAsString(), "args");
        methodDeclaration.setType(outputDtoJava.getMainTypeDeclaration().getNameAsString());

        BlockStmt methodBody = methodDeclaration.getBody().orElseThrow();
        methodBody.addStatement(
                outputDtoJava.getMainTypeDeclaration().getNameAsString()
                        + " result = "
                        + outputDtoJava.getMainTypeDeclaration().getNameAsString()
                        + ".builder().build();");
        methodBody.addStatement("return result;");
    }

    private void addInterfaceMethod(JavaDocument interfaceJava, SpringServiceMethod methodSpec, JavaDocument inputDtoJava, JavaDocument outputDtoJava) {
        CompilationUnit interfaceUnit = interfaceJava.getCompilationUnit();
        interfaceUnit.addImport(GenUtility.joinPackageName(inputDtoJava.getPackageName(), inputDtoJava.getMainTypeDeclaration().getNameAsString()));
        interfaceUnit.addImport(GenUtility.joinPackageName(outputDtoJava.getPackageName(), outputDtoJava.getMainTypeDeclaration().getNameAsString()));

        ClassOrInterfaceDeclaration interfaceDeclaration = (ClassOrInterfaceDeclaration) interfaceJava.getMainTypeDeclaration();
        MethodDeclaration methodDeclaration =  interfaceDeclaration.addMethod(methodSpec.getMethodName());
        methodDeclaration.addParameter(inputDtoJava.getMainTypeDeclaration().getNameAsString(), "args");
        methodDeclaration.setType(outputDtoJava.getMainTypeDeclaration().getNameAsString());
        methodDeclaration.removeBody();
    }

    private TextFileWrapper maybeGenerateResultDtoClass(String typeFullName) {
        return SpringDtoGenerator.generateRestDto(genCtx, genCtx.getProjectSpec().getSrcMainJava(), typeFullName);
    }

    private TextFileWrapper maybeGenerateArgumentsDtoClass(String typeFullName) {
        return SpringDtoGenerator.generateRestDto(genCtx, genCtx.getProjectSpec().getSrcMainJava(), typeFullName);
    }

    private TextFileWrapper maybeGenerateServiceImplementation(String typeFullName, JavaDocument interfaceJava) {
        Pair<JavaDocument, CompilationUnit> docUnit = GenUtility.maybeCreateJavaClass(
                genCtx.getProjectSpec().getSrcMainJava(), typeFullName, genCtx.getConfig().isForce());
        CompilationUnit cUnit = docUnit.getRight();
        if (cUnit != null) {
            cUnit.addImport("lombok.RequiredArgsConstructor");
            cUnit.addImport("org.springframework.stereotype.Service");
            cUnit.addImport(GenUtility.joinPackageName(interfaceJava.getPackageName(), interfaceJava.getMainTypeDeclaration().getNameAsString()));

            ClassOrInterfaceDeclaration ciDeclaration = (ClassOrInterfaceDeclaration) docUnit.getLeft().getMainTypeDeclaration();
            ciDeclaration.addAnnotation("RequiredArgsConstructor");
            ciDeclaration.addAnnotation("Service");
            ciDeclaration.addImplementedType(interfaceJava.getMainTypeDeclaration().getNameAsString());
        }
        return docUnit.getLeft();
    }

    private TextFileWrapper maybeGenerateServiceInterface(String typeFullName) {
        Pair<JavaDocument, CompilationUnit> docUnit = GenUtility.maybeCreateJavaInterface(
                genCtx.getProjectSpec().getSrcMainJava(), typeFullName, genCtx.getConfig().isForce());
        CompilationUnit cUnit = docUnit.getRight();
        if (cUnit != null) {
            docUnit.getLeft().getMainTypeDeclaration();
        }
        return docUnit.getLeft();
    }


}
