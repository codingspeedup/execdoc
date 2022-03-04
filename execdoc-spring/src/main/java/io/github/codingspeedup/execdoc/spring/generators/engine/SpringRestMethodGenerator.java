package io.github.codingspeedup.execdoc.spring.generators.engine;

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
import io.github.codingspeedup.execdoc.spring.generators.SpringBootGenCfg;
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

public class SpringRestMethodGenerator implements Serializable {

    private final SpringBootGenCfg genCfg;
    private final SpringBootProjectSpec projectSpec;

    @Getter
    private final Map<String, TextFileWrapper> artifacts;

    public SpringRestMethodGenerator(SpringBootGenCfg genCfg, SpringBootProjectSpec projectSpec) {
        this(genCfg, projectSpec, null);
    }

    public SpringRestMethodGenerator(SpringBootGenCfg genCfg, SpringBootProjectSpec projectSpec, Map<String, TextFileWrapper> artifacts) {
        this.genCfg = genCfg;
        this.projectSpec = projectSpec;
        this.artifacts = artifacts == null ? new LinkedHashMap<>() : artifacts;
    }

    public static String getDefaultResponseStatus(SpringRestMethodSpec methodSpec) {
        switch (methodSpec.getHttpRequestMethod()) {
            case POST:
                return "CREATED";
            case DELETE:
            case PATCH:
                return "NO_CONTENT";
            default:
                return "OK";
        }
    }

    public void generateArtifacts(SpringRestMethodSpec methodSpec) {
        JavaDocument controllerJava = (JavaDocument) artifacts.computeIfAbsent(GenUtility.joinPackageName(methodSpec.getPackageName(), methodSpec.getTypeName()), key -> maybeGenerateControllerClass(key, methodSpec));
        ClassOrInterfaceDeclaration controllerDeclaration = (ClassOrInterfaceDeclaration) controllerJava.getMainTypeDeclaration();
        List<MethodDeclaration> methodDeclarations = controllerDeclaration.getMethodsByName(methodSpec.getMethodName());
        if (CollectionUtils.isEmpty(methodDeclarations)) {
            JavaDocument requestDtoJava = (JavaDocument) artifacts.computeIfAbsent(GenUtility.joinPackageName(methodSpec.getDtoPackageName(), methodSpec.getDtoInputTypeName()), key -> maybeGenerateRequestDtoClass(key, methodSpec));
            JavaDocument responseDtoJava = (JavaDocument) artifacts.computeIfAbsent(GenUtility.joinPackageName(methodSpec.getDtoPackageName(), methodSpec.getDtoOutputTypeName()), key -> maybeGenerateResponseDtoClass(key, methodSpec));
            addControllerMethod(controllerJava, methodSpec, requestDtoJava, responseDtoJava);
            JavaDocument controllerTestJava = (JavaDocument) artifacts.computeIfAbsent(GenUtility.joinPackageName(projectSpec.getIntegrationTestsPackageName(), methodSpec.getTypeName() + "ITest"), key -> maybeGenerateControllerITestClass(key, methodSpec, controllerJava));
            addITestMethod(controllerTestJava, methodSpec, requestDtoJava, responseDtoJava);
        }
    }

    private void addITestMethod(JavaDocument controllerTestJava, SpringRestMethodSpec methodSpec, JavaDocument requestDtoJava, JavaDocument responseDtoJava) {
        CompilationUnit testUnit = controllerTestJava.getCompilationUnit();
        testUnit.addImport("org.springframework.http.HttpStatus");
        testUnit.addImport("org.springframework.http.ResponseEntity");

        ClassOrInterfaceDeclaration testTypeDeclaration = (ClassOrInterfaceDeclaration) controllerTestJava.getMainTypeDeclaration();

        MethodDeclaration methodDeclaration = testTypeDeclaration.addMethod("test" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, methodSpec.getMethodName()));
        methodDeclaration.addAnnotation("Test");
        BlockStmt methodBody = methodDeclaration.getBody().orElseThrow();

        StringBuilder callStatement = new StringBuilder(" restResponse = controller.").append(methodSpec.getMethodName()).append("(");

        if (requestDtoJava != null) {
            String requestBodyTypeName = requestDtoJava.getMainTypeDeclaration().getNameAsString();
            testUnit.addImport(GenUtility.joinPackageName(methodSpec.getDtoPackageName(), requestBodyTypeName));
            methodBody.addStatement(methodSpec.getDtoInputTypeName() + " restRequest = new " + requestBodyTypeName + "();");
            callStatement.append("restRequest");
        }

        callStatement.append(");");

        if (responseDtoJava != null) {
            String responseBodyTypeName = responseDtoJava.getMainTypeDeclaration().getNameAsString();
            testUnit.addImport(GenUtility.joinPackageName(methodSpec.getDtoPackageName(), responseBodyTypeName));
            callStatement.insert(0, "ResponseEntity<" + responseBodyTypeName + ">");
        } else {
            callStatement.insert(0, "ResponseEntity<?>");
        }

        methodBody.addStatement(callStatement.toString());

        if (BooleanUtils.isTrue(methodSpec.getHttpRequestMethod().getHasResponseBody())) {
            testUnit.addImport("org.junit.jupiter.api.Assertions.assertEquals", true, false);
            methodBody.addStatement("assertEquals(HttpStatus." + getDefaultResponseStatus(methodSpec) + ", restResponse.getStatusCode());");
        }
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
        if (responseDtoJava != null) {
            String responseBodyTypeName = responseDtoJava.getMainTypeDeclaration().getNameAsString();
            controllerUnit.addImport(GenUtility.joinPackageName(responseDtoJava.getPackageName(), responseBodyTypeName));
            methodDeclaration.setType("ResponseEntity<" + responseBodyTypeName + ">");
            methodBody.addStatement(responseBodyTypeName + " restResponse = new " + responseBodyTypeName + "();");
            methodBody.addStatement("return new ResponseEntity<>(restResponse, HttpStatus." + getDefaultResponseStatus(methodSpec) + ");");
        } else {
            methodDeclaration.setType("ResponseEntity<?>");
            methodBody.addStatement("return new ResponseEntity<>(HttpStatus." + getDefaultResponseStatus(methodSpec) + ");");
        }
    }

    private JavaDocument maybeGenerateControllerITestClass(String typeFullName, SpringRestMethodSpec methodSpec, JavaDocument controllerJava) {
        String[] packageType = GenUtility.splitTypeFullName(typeFullName);
        File controllerTestFile = GenUtility.fileOf(projectSpec.getSrcTestJava(), packageType[0], packageType[1] + ".java");
        JavaDocument controllerTestJava = new JavaDocument(controllerTestFile);
        if (!controllerTestFile.exists()) {
            CompilationUnit controllerUnit = controllerTestJava.getCompilationUnit();
            controllerUnit.setPackageDeclaration(packageType[0]);

            controllerUnit.addImport("org.junit.jupiter.api.Test");
            controllerUnit.addImport("org.junit.jupiter.api.extension.ExtendWith");
            controllerUnit.addImport("org.springframework.beans.factory.annotation.Autowired");
            controllerUnit.addImport("org.springframework.boot.test.context.SpringBootTest");
            controllerUnit.addImport("org.springframework.test.context.junit.jupiter.SpringExtension");

            ClassOrInterfaceDeclaration ciDeclaration = controllerUnit.addClass(packageType[1]);
            ciDeclaration.addSingleMemberAnnotation("ExtendWith", "SpringExtension.class");
            ciDeclaration.addAnnotation("SpringBootTest");

            controllerUnit.addImport(GenUtility.joinPackageName(controllerJava.getPackageName(), controllerJava.getMainTypeDeclaration().getNameAsString()));
            ciDeclaration.addField(methodSpec.getTypeName(), "controller").addAnnotation("Autowired");
        }
        return controllerTestJava;
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
