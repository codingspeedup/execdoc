package io.github.codingspeedup.execdoc.spring.generators.artifacts;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
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
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.javaparser.ast.Modifier.Keyword;

public class RestMethodGenerator implements Serializable {

    private final SpringBootProjectSpec projectSpec;

    @Getter
    private final Map<String, TextFileWrapper> artifacts;

    public RestMethodGenerator(SpringBootProjectSpec projectSpec) {
        this(projectSpec, null);
    }

    public RestMethodGenerator(SpringBootProjectSpec projectSpec, Map<String, TextFileWrapper> artifacts) {
        this.projectSpec = projectSpec;
        this.artifacts = artifacts == null ? new LinkedHashMap<>() : artifacts;
    }

    public void generateArtifacts(SpringRestMethodSpec methodSpec) {
        JavaDocument controllerJava = (JavaDocument) artifacts.computeIfAbsent(GenUtility.joinPackageName(methodSpec.getPackageName(), methodSpec.getTypeName()), key -> maybeGenerateControllerClass(key, methodSpec));
        List<MethodDeclaration> methodDeclarations = controllerJava.getCompilationUnit()
                .getClassByName(methodSpec.getTypeName()).orElseThrow(() -> new UnsupportedOperationException("Controller class not found " + methodSpec.getTypeName()))
                .getMethodsByName(methodSpec.getMethodName());
        if (CollectionUtils.isEmpty(methodDeclarations)) {
            JavaDocument requestDtoJava = (JavaDocument) artifacts.computeIfAbsent(GenUtility.joinPackageName(methodSpec.getDtoPackageName(), methodSpec.getDtoInputTypeName()), key -> maybeGenerateRequestDtoClass(key, methodSpec));
            JavaDocument responseDtoJava = (JavaDocument) artifacts.computeIfAbsent(GenUtility.joinPackageName(methodSpec.getDtoPackageName(), methodSpec.getDtoOutputTypeName()), key -> maybeGenerateResponseDtoClass(key, methodSpec));
            addControllerMethod(controllerJava, methodSpec, requestDtoJava, responseDtoJava);
            JavaDocument controllerTestJava = (JavaDocument) artifacts.computeIfAbsent(GenUtility.joinPackageName(methodSpec.getPackageName(), methodSpec.getTypeName() + "ITest"), key -> maybeGenerateControllerTestClass(key, methodSpec));
            addTestMethod(controllerTestJava, methodSpec);
        }
    }

    private void addTestMethod(JavaDocument controllerTestJava, SpringRestMethodSpec methodSpec) {
        CompilationUnit cUnit = controllerTestJava.getCompilationUnit();
        ClassOrInterfaceDeclaration ciDeclaration = (ClassOrInterfaceDeclaration) controllerTestJava.getMainTypeDeclaration();

        MethodDeclaration methodDeclaration = ciDeclaration.addMethod("test" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, methodSpec.getMethodName()));
        methodDeclaration.addAnnotation("Test");
        BlockStmt methodBody = methodDeclaration.getBody().orElseThrow();

        StringBuilder callStatement = new StringBuilder("controller.").append(methodSpec.getMethodName()).append("(");

        if (BooleanUtils.isTrue(methodSpec.getHttpRequestMethod().getHasRequestBody())) {
            cUnit.addImport(GenUtility.joinPackageName(methodSpec.getDtoPackageName(), methodSpec.getDtoInputTypeName()));
            methodBody.addStatement(methodSpec.getDtoInputTypeName() + " restRequest = new " + methodSpec.getDtoInputTypeName() + "();");
            callStatement.append("restRequest");
        }

        callStatement.append(");");

        if (BooleanUtils.isTrue(methodSpec.getHttpRequestMethod().getHasResponseBody())) {
            cUnit.addImport(GenUtility.joinPackageName(methodSpec.getDtoPackageName(), methodSpec.getDtoOutputTypeName()));
            callStatement.insert(0, " restResponse = ");
            callStatement.insert(0, methodSpec.getDtoOutputTypeName());
        }

        methodBody.addStatement(callStatement.toString());

        if (BooleanUtils.isTrue(methodSpec.getHttpRequestMethod().getHasResponseBody())) {
            cUnit.addImport("org.junit.jupiter.api.Assertions.assertNotNull", true, false);
            methodBody.addStatement("assertNotNull(restResponse);");
        }
    }

    private JavaDocument maybeGenerateControllerTestClass(String typeFullName, SpringRestMethodSpec methodSpec) {
        String[] packageType = GenUtility.splitTypeFullName(typeFullName);
        File controllerTestFile = GenUtility.fileOf(projectSpec.getSrcTestJava(), packageType[0], packageType[1] + ".java");
        JavaDocument controllerTestJava = new JavaDocument(controllerTestFile);
        if (!controllerTestFile.exists()) {
            CompilationUnit cUnit = controllerTestJava.getCompilationUnit();
            cUnit.setPackageDeclaration(packageType[0]);

            cUnit.addImport("org.junit.jupiter.api.Test");
            cUnit.addImport("org.junit.jupiter.api.extension.ExtendWith");
            cUnit.addImport("org.springframework.beans.factory.annotation.Autowired");
            cUnit.addImport("org.springframework.boot.test.context.SpringBootTest");
            cUnit.addImport("org.springframework.test.context.junit.jupiter.SpringExtension");

            ClassOrInterfaceDeclaration ciDeclaration = cUnit.addClass(packageType[1]);
            ciDeclaration.addSingleMemberAnnotation("ExtendWith", "SpringExtension.class");
            ciDeclaration.addAnnotation("SpringBootTest");

            ciDeclaration.addField(methodSpec.getTypeName(), "controller").addAnnotation("Autowired");
        }
        return controllerTestJava;
    }

    private void addControllerMethod(JavaDocument controllerJava, SpringRestMethodSpec methodSpec, JavaDocument requestDtoJava, JavaDocument responseDtoJava) {
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
        BlockStmt methodBody = methodDeclaration.getBody().orElseThrow();

        if (requestDtoJava != null) {
            controllerUnit.addImport("org.springframework.web.bind.annotation.RequestBody");
            controllerUnit.addImport("org.springframework.validation.annotation.Validated");
            String requestBodyTypeName = requestDtoJava.getMainTypeDeclaration().getNameAsString();
            controllerUnit.addImport(GenUtility.joinPackageName(requestDtoJava.getPackageName(), requestBodyTypeName));
            Parameter requestBodyParameter = methodDeclaration.addAndGetParameter(requestBodyTypeName, "restRequest");
            requestBodyParameter.addAnnotation("RequestBody");
            requestBodyParameter.addAnnotation("Validated");
        }

        controllerUnit.addImport("org.springframework.http.HttpStatus");
        controllerUnit.addImport("org.springframework.http.ResponseEntity");
        String responseStatus;
        switch (methodSpec.getHttpRequestMethod()) {
            case POST:
                responseStatus = "CREATED";
                break;
            case DELETE:
            case PATCH:
                responseStatus = "NO_CONTENT";
                break;
            default:
                responseStatus = "OK";
        }
        if (responseDtoJava != null) {
            String responseBodyTypeName = responseDtoJava.getMainTypeDeclaration().getNameAsString();
            controllerUnit.addImport(GenUtility.joinPackageName(responseDtoJava.getPackageName(), responseBodyTypeName));
            methodDeclaration.setType("ResponseEntity<" + responseBodyTypeName + ">");
            methodBody.addStatement(responseBodyTypeName + " restResponse = new " + responseBodyTypeName + "();");
            methodBody.addStatement("return new ResponseEntity<>(restResponse, HttpStatus." + responseStatus + ");");
        } else {
            methodDeclaration.setType("ResponseEntity<?>");
            methodBody.addStatement("return new ResponseEntity<>(HttpStatus." + responseStatus + ");");
        }
    }

    private JavaDocument maybeGenerateRequestDtoClass(String typeFullName, SpringRestMethodSpec methodSpec) {
        if (BooleanUtils.isNotTrue(methodSpec.getHttpRequestMethod().getHasRequestBody())) {
            return null;
        }
        return GenUtility.generateDto(projectSpec.getSrcMainJava(), typeFullName);
    }

    private JavaDocument maybeGenerateResponseDtoClass(String typeFullName, SpringRestMethodSpec methodSpec) {
        if (BooleanUtils.isNotTrue(methodSpec.getHttpRequestMethod().getHasResponseBody())) {
            return null;
        }
        switch (methodSpec.getHttpRequestMethod()) {
            case DELETE:
            case PATCH:
                return null;
        }
        return GenUtility.generateDto(projectSpec.getSrcMainJava(), typeFullName);
    }

    private JavaDocument maybeGenerateControllerClass(String typeFullName, SpringRestMethodSpec methodSpec) {
        String[] packageType = GenUtility.splitTypeFullName(typeFullName);
        File controllerFile = GenUtility.fileOf(projectSpec.getSrcMainJava(), packageType[0], packageType[1] + ".java");
        JavaDocument controllerJava = new JavaDocument(controllerFile);
        if (!controllerFile.exists()) {
            CompilationUnit cUnit = controllerJava.getCompilationUnit();
            cUnit.setPackageDeclaration(packageType[0]);

            cUnit.addImport("lombok.RequiredArgsConstructor");
            cUnit.addImport("lombok.extern.slf4j.Slf4j");
            cUnit.addImport("org.springframework.web.bind.annotation.RequestMapping");
            cUnit.addImport("org.springframework.web.bind.annotation.RestController");

            ClassOrInterfaceDeclaration ciDeclaration = cUnit.addClass(packageType[1], Keyword.PUBLIC);
            ciDeclaration.addAnnotation("RestController");
            ciDeclaration.addSingleMemberAnnotation("RequestMapping", packageType[1] + ".ROOT_URI");
            ciDeclaration.addAnnotation("RequiredArgsConstructor");
            ciDeclaration.addAnnotation("Slf4j");

            GenUtility.addCreationJavadoc(ciDeclaration);

            String rootUri = "/" + projectSpec.getRestApiRoot() + "/" + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, methodSpec.getTypeLemma());
            ciDeclaration.addFieldWithInitializer(String.class, "ROOT_URI", new StringLiteralExpr(rootUri), Keyword.STATIC, Keyword.FINAL);
        }
        return controllerJava;
    }

}
