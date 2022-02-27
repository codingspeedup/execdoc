package io.github.codingspeedup.execdoc.spring.generators.artifacts;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.google.common.base.CaseFormat;
import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;
import io.github.codingspeedup.execdoc.spring.generators.spec.SpringBootProjectSpec;
import io.github.codingspeedup.execdoc.spring.generators.spec.SpringRestMethodSpec;
import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaDocument;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.github.javaparser.ast.Modifier.Keyword;

public class RestMethodGenerator implements Serializable {

    private final SpringBootProjectSpec projectSpec;
    private final SpringRestMethodSpec methodSpec;

    public RestMethodGenerator(SpringBootProjectSpec projectSpec, SpringRestMethodSpec methodSpec) {
        this.projectSpec = projectSpec;
        this.methodSpec = methodSpec;
    }

    public List<TextFileWrapper> generateArtifacts() {
        List<TextFileWrapper> artifacts = new ArrayList<>();
        JavaDocument controllerJava = maybeGenerateControllerClass();
        List<MethodDeclaration> methodDeclarations = controllerJava.getCompilationUnit()
                .getClassByName(methodSpec.getTypeSimpleName()).orElseThrow(() -> new UnsupportedOperationException("Controller class not found " + methodSpec.getTypeSimpleName()))
                .getMethodsByName(methodSpec.getMethodName());
        if (CollectionUtils.isEmpty(methodDeclarations)) {
            artifacts.add(controllerJava);
            JavaDocument requestDtoJava = maybeGenerateRequestDtoClass();
            if (requestDtoJava != null) {
                artifacts.add(requestDtoJava);
            }
            JavaDocument responseDtoJava = maybeGenerateResponseDtoClass();
            if (responseDtoJava != null) {
                artifacts.add(responseDtoJava);
            }
            addControllerMethod(controllerJava, requestDtoJava, responseDtoJava);
            JavaDocument controllerTestJava = maybeGenerateControllerTestClass();
            artifacts.add(controllerTestJava);
        }
        return artifacts;
    }

    private JavaDocument maybeGenerateControllerTestClass() {
        String testSimpleName = methodSpec.getTypeSimpleName() + "ITest";
        File controllerTestFile = GenUtility.fileOf(projectSpec.getSrcTestJava(), methodSpec.getPackageName(), methodSpec.getTypeSimpleName() + ".java");
        JavaDocument controllerTestJava = new JavaDocument(controllerTestFile);
        if (!controllerTestFile.exists()) {
            CompilationUnit cUnit = controllerTestJava.getCompilationUnit();
            cUnit.setPackageDeclaration(methodSpec.getPackageName());

            cUnit.addImport("org.junit.jupiter.api.Test");
            cUnit.addImport("org.junit.jupiter.api.extension.ExtendWith");
            cUnit.addImport("org.springframework.beans.factory.annotation.Autowired");
            cUnit.addImport("org.springframework.boot.test.context.SpringBootTest");
            cUnit.addImport("org.springframework.test.context.junit.jupiter.SpringExtension");

            ClassOrInterfaceDeclaration ciDeclaration = cUnit.addClass(testSimpleName);
            ciDeclaration.addSingleMemberAnnotation("ExtendWith", "SpringExtension.class");
            ciDeclaration.addAnnotation("SpringBootTest");

            ciDeclaration.addField(methodSpec.getTypeSimpleName(), "controller").addAnnotation("Autowired");

            MethodDeclaration methodDeclaration = ciDeclaration.addMethod("test" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, methodSpec.getMethodName()));
            methodDeclaration.addAnnotation("Test");
            BlockStmt methodBody = methodDeclaration.getBody().orElseThrow();

            StringBuilder callStatement = new StringBuilder("controller.").append(methodSpec.getMethodName()).append("(");

            if (BooleanUtils.isTrue(methodSpec.getHttpRequestMethod().getHasRequestBody())) {
                cUnit.addImport(GenUtility.joinPackageName(methodSpec.getDtoPackageName(), methodSpec.getDtoInputTypeSimpleName()));
                methodBody.addStatement(methodSpec.getDtoInputTypeSimpleName() + " restRequest = new " + methodSpec.getDtoInputTypeSimpleName() + "();");
                callStatement.append("restRequest");
            }

            callStatement.append(");");

            if (BooleanUtils.isTrue(methodSpec.getHttpRequestMethod().getHasResponseBody())) {
                cUnit.addImport(GenUtility.joinPackageName(methodSpec.getDtoPackageName(), methodSpec.getDtoOutputTypeSimpleName()));
                callStatement.insert(0, " restResponse = ");
                callStatement.insert(0, methodSpec.getDtoOutputTypeSimpleName());
            }

            methodBody.addStatement(callStatement.toString());

            if (BooleanUtils.isTrue(methodSpec.getHttpRequestMethod().getHasResponseBody())) {
                cUnit.addImport("org.junit.jupiter.api.Assertions.assertNotNull", true, false);
                methodBody.addStatement("assertNotNull(restResponse);");
            }
        }
        return controllerTestJava;
    }

    private void addControllerMethod(JavaDocument controllerJava, JavaDocument requestDtoJava, JavaDocument responseDtoJava) {
        String mappingName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, methodSpec.getHttpRequestMethod().name()) + "Mapping";
        String restUri = "URI_" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, methodSpec.getMethodName());
        String methodUri = "METHOD_" + restUri;

        CompilationUnit controllerUnit = controllerJava.getCompilationUnit();
        ClassOrInterfaceDeclaration controllerDeclaration = (ClassOrInterfaceDeclaration) controllerJava.getMainTypeDeclaration();

        controllerDeclaration.addFieldWithInitializer(String.class, methodUri,
                new StringLiteralExpr("/" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, methodSpec.getMethodName())),
                Keyword.PRIVATE, Keyword.STATIC, Keyword.FINAL);

        controllerDeclaration.addFieldWithInitializer(String.class, restUri,
                new BinaryExpr(new NameExpr("ROOT_URI"), new NameExpr(methodUri), BinaryExpr.Operator.PLUS),
                Keyword.PUBLIC, Keyword.STATIC, Keyword.FINAL);

        MethodDeclaration methodDeclaration = controllerDeclaration.addMethod(methodSpec.getMethodName(), Keyword.PUBLIC);
        controllerUnit.addImport("org.springframework.web.bind.annotation." + mappingName);
        methodDeclaration.addSingleMemberAnnotation(mappingName, methodUri);

        if (requestDtoJava != null) {
            String requestDtoName = requestDtoJava.getMainTypeDeclaration().getNameAsString();
            controllerUnit.addImport("org.springframework.web.bind.annotation.RequestBody");
            controllerUnit.addImport(GenUtility.joinPackageName(requestDtoJava.getPackageName(), requestDtoName));
            methodDeclaration.addAndGetParameter(requestDtoName, "restRequest").addAnnotation("RequestBody");
        }

        if (responseDtoJava != null) {
            String responseDtoName = responseDtoJava.getMainTypeDeclaration().getNameAsString();
            controllerUnit.addImport("org.springframework.web.bind.annotation.ResponseBody");
            controllerUnit.addImport(GenUtility.joinPackageName(responseDtoJava.getPackageName(), responseDtoName));
            methodDeclaration.addAnnotation("ResponseBody");
            methodDeclaration.setType(responseDtoName);

            BlockStmt methodBody = methodDeclaration.getBody().orElseThrow();
            methodBody.addStatement(responseDtoName + " restResponse = new " + responseDtoName + "();");
            methodBody.addStatement("return restResponse;");
        }
    }

    private JavaDocument maybeGenerateRequestDtoClass() {
        if (BooleanUtils.isNotTrue(methodSpec.getHttpRequestMethod().getHasRequestBody())) {
            return null;
        }
        File requestDtoFile = GenUtility.fileOf(projectSpec.getSrcMainJava(), methodSpec.getDtoPackageName(), methodSpec.getDtoInputTypeSimpleName() + ".java");
        JavaDocument requestDtoJava = new JavaDocument(requestDtoFile);
        if (!requestDtoFile.exists()) {
            CompilationUnit cUnit = requestDtoJava.getCompilationUnit();
            cUnit.setPackageDeclaration(methodSpec.getDtoPackageName());
            ClassOrInterfaceDeclaration ciDeclaration = cUnit.addClass(methodSpec.getDtoInputTypeSimpleName(), Keyword.PUBLIC);
            GenUtility.makeSerializable(ciDeclaration);
            GenUtility.addLombokGettersAndSetters(ciDeclaration);
        }
        return requestDtoJava;
    }

    private JavaDocument maybeGenerateResponseDtoClass() {
        if (BooleanUtils.isNotTrue(methodSpec.getHttpRequestMethod().getHasResponseBody())) {
            return null;
        }
        File requestDtoFile = GenUtility.fileOf(projectSpec.getSrcMainJava(), methodSpec.getDtoPackageName(), methodSpec.getDtoOutputTypeSimpleName() + ".java");
        JavaDocument requestDtoJava = new JavaDocument(requestDtoFile);
        if (!requestDtoFile.exists()) {
            CompilationUnit cUnit = requestDtoJava.getCompilationUnit();
            cUnit.setPackageDeclaration(methodSpec.getDtoPackageName());
            ClassOrInterfaceDeclaration ciDeclaration = cUnit.addClass(methodSpec.getDtoOutputTypeSimpleName(), Keyword.PUBLIC);
            GenUtility.makeSerializable(ciDeclaration);
            GenUtility.addLombokGettersAndSetters(ciDeclaration);
        }
        return requestDtoJava;
    }

    private JavaDocument maybeGenerateControllerClass() {
        File controllerFile = GenUtility.fileOf(projectSpec.getSrcMainJava(), methodSpec.getPackageName(), methodSpec.getTypeSimpleName() + ".java");
        JavaDocument controllerJava = new JavaDocument(controllerFile);
        if (!controllerFile.exists()) {
            CompilationUnit cUnit = controllerJava.getCompilationUnit();
            cUnit.setPackageDeclaration(methodSpec.getPackageName());

            cUnit.addImport("lombok.extern.slf4j.Slf4j");
            cUnit.addImport("org.springframework.web.bind.annotation.RequestMapping");
            cUnit.addImport("org.springframework.web.bind.annotation.RestController");

            ClassOrInterfaceDeclaration ciDeclaration = cUnit.addClass(methodSpec.getTypeSimpleName(), Keyword.PUBLIC);
            ciDeclaration.addAnnotation("RestController");
            ciDeclaration.addSingleMemberAnnotation("RequestMapping", methodSpec.getTypeSimpleName() + ".ROOT_URI");
            ciDeclaration.addAnnotation("Slf4j");

            String rootUri = "/" + projectSpec.getRestApiRoot() + "/" + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, methodSpec.getTypeLemma());
            ciDeclaration.addFieldWithInitializer(String.class, "ROOT_URI", new StringLiteralExpr(rootUri), Keyword.STATIC, Keyword.FINAL);
        }
        return controllerJava;
    }

}
