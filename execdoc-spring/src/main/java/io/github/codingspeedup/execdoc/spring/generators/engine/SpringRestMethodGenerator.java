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
import io.github.codingspeedup.execdoc.spring.generators.SpringGenCtx;
import io.github.codingspeedup.execdoc.spring.generators.spec.HttpRequestMethod;
import io.github.codingspeedup.execdoc.spring.generators.spec.SpringRestMethodSpec;
import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaDocument;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.github.javaparser.ast.Modifier.Keyword;

public class SpringRestMethodGenerator extends AbstractSpringGenerator {

    public SpringRestMethodGenerator(SpringGenCtx genCtx) {
        super(genCtx);
    }

    public SpringRestMethodGenerator(SpringGenCtx genCtx, Map<String, TextFileWrapper> artifacts) {
        super(genCtx, artifacts);
    }

    public static String getDefaultResponseStatus(HttpRequestMethod httpMethod) {
        switch (httpMethod) {
            case POST:
                return "CREATED";
            case DELETE:
            case PATCH:
                return "NO_CONTENT";
            default:
                return "OK";
        }
    }

    private static String getRestUri(String methodName) {
        return "URI_" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, methodName);
    }

    public Map<String, TextFileWrapper> generateArtifacts(SpringRestMethodSpec methodSpec) {
        JavaDocument controllerJava = (JavaDocument) getArtifacts().computeIfAbsent(
                GenUtility.joinPackageName(methodSpec.getPackageName(), methodSpec.getTypeName()),
                key -> maybeGenerateControllerClass(key, methodSpec));
        ClassOrInterfaceDeclaration controllerDeclaration = (ClassOrInterfaceDeclaration) controllerJava.getMainTypeDeclaration();
        List<MethodDeclaration> methodDeclarations = controllerDeclaration.getMethodsByName(methodSpec.getMethodName());
        if (CollectionUtils.isEmpty(methodDeclarations)) {
            JavaDocument inputDtoJava = (JavaDocument) getArtifacts().computeIfAbsent(
                    GenUtility.joinPackageName(methodSpec.getDtoPackageName(), methodSpec.getDtoInputTypeName()),
                    key -> maybeGenerateRequestDtoClass(key, methodSpec));
            JavaDocument outputDtoJava = (JavaDocument) getArtifacts().computeIfAbsent(
                    GenUtility.joinPackageName(methodSpec.getDtoPackageName(), methodSpec.getDtoOutputTypeName()),
                    key -> maybeGenerateResponseDtoClass(key, methodSpec));

            addControllerMethod(controllerJava, methodSpec, inputDtoJava, outputDtoJava);

            JavaDocument controllerTestJava = (JavaDocument) getArtifacts().computeIfAbsent(
                    GenUtility.joinPackageName(controllerJava.getPackageName(), methodSpec.getTypeName() + "Test"),
                    key -> maybeGenerateControllerTestClass(key, controllerJava));
            addTestMethod(controllerTestJava, methodSpec, controllerJava, inputDtoJava, outputDtoJava);

            JavaDocument controllerITestJava = (JavaDocument) getArtifacts().computeIfAbsent(
                    GenUtility.joinPackageName(getProjectSpec().getRestIntegrationTestsPackageName(), methodSpec.getTypeName() + "IT"),
                    key -> maybeGenerateControllerITestClass(key, controllerJava));
            addITestMethod(controllerITestJava, methodSpec, inputDtoJava, outputDtoJava);
        }
        return getArtifacts();
    }

    private void addITestMethod(JavaDocument controllerITestJava, SpringRestMethodSpec methodSpec, JavaDocument requestJava, JavaDocument responseJava) {
        CompilationUnit testUnit = controllerITestJava.getCompilationUnit();
        testUnit.addImport("org.springframework.http.HttpStatus");
        testUnit.addImport("org.springframework.http.ResponseEntity");

        ClassOrInterfaceDeclaration testTypeDeclaration = (ClassOrInterfaceDeclaration) controllerITestJava.getMainTypeDeclaration();

        MethodDeclaration methodDeclaration = testTypeDeclaration.addMethod("test" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, methodSpec.getMethodName()));
        methodDeclaration.addAnnotation("Test");
        BlockStmt methodBody = methodDeclaration.getBody().orElseThrow();

        StringBuilder callStatement = new StringBuilder(" restResponse = controller.").append(methodSpec.getMethodName()).append("(");

        if (requestJava != null) {
            String requestTypeName = requestJava.getMainTypeDeclaration().getNameAsString();
            testUnit.addImport(GenUtility.joinPackageName(methodSpec.getDtoPackageName(), requestTypeName));
            methodBody.addStatement(methodSpec.getDtoInputTypeName() + " restRequest = new " + requestTypeName + "();");
            callStatement.append("restRequest");
        }

        callStatement.append(");");

        if (responseJava != null) {
            String responseBodyTypeName = responseJava.getMainTypeDeclaration().getNameAsString();
            testUnit.addImport(GenUtility.joinPackageName(methodSpec.getDtoPackageName(), responseBodyTypeName));
            callStatement.insert(0, "ResponseEntity<" + responseBodyTypeName + ">");
        } else {
            callStatement.insert(0, "ResponseEntity<?>");
        }

        methodBody.addStatement(callStatement.toString());

        if (BooleanUtils.isTrue(methodSpec.getHttpRequestMethod().getHasResponseBody())) {
            testUnit.addImport("org.junit.jupiter.api.Assertions.assertEquals", true, false);
            methodBody.addStatement("assertEquals(HttpStatus." + getDefaultResponseStatus(methodSpec.getHttpRequestMethod()) + ", restResponse.getStatusCode());");
        }
    }

    private void addTestMethod(JavaDocument controllerTestJava, SpringRestMethodSpec methodSpec, JavaDocument controllerJava, JavaDocument requestJava, JavaDocument responseJava) {
        CompilationUnit testUnit = controllerTestJava.getCompilationUnit();

        ClassOrInterfaceDeclaration testTypeDeclaration = (ClassOrInterfaceDeclaration) controllerTestJava.getMainTypeDeclaration();

        MethodDeclaration methodDeclaration = testTypeDeclaration.addMethod("test" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, methodSpec.getMethodName()));
        methodDeclaration.addAnnotation("Test");
        methodDeclaration.addThrownException(Exception.class);

        BlockStmt methodBody = methodDeclaration.getBody().orElseThrow();

        if (requestJava != null) {
            String requestTypeName = requestJava.getMainTypeDeclaration().getNameAsString();
            testUnit.addImport(GenUtility.joinPackageName(methodSpec.getDtoPackageName(), requestTypeName));
            methodBody.addStatement(requestTypeName + " requestDto = " + requestTypeName + ".builder().build();");
            methodBody.addStatement("String requestDtoJson = objectMapper.writeValueAsString(requestDto);");
        }

        StringBuilder callStatement = new StringBuilder("mockMvc.perform(");
        callStatement.append(methodSpec.getHttpRequestMethod().name().toLowerCase(Locale.ROOT)).append("(").append(controllerJava.getMainTypeDeclaration().getNameAsString()).append(".").append(getRestUri(methodSpec.getMethodName())).append(")");
        if (requestJava != null) {
            callStatement.append(".contentType(MediaType.APPLICATION_JSON)");
            callStatement.append(".content(requestDtoJson)");
        }
        callStatement.append(").andExpect(status().is").append(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, getDefaultResponseStatus(methodSpec.getHttpRequestMethod()))).append("());");

        methodBody.addStatement(callStatement.toString());
    }

    private void addControllerMethod(JavaDocument controllerJava, SpringRestMethodSpec methodSpec, JavaDocument requestDtoJava, JavaDocument responseDtoJava) {
        String mappingName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, methodSpec.getHttpRequestMethod().name()) + "Mapping";
        String restUri = getRestUri(methodSpec.getMethodName());
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
            methodBody.addStatement(responseBodyTypeName + " restResponse = " + responseBodyTypeName + ".builder().build();");
            methodBody.addStatement("return new ResponseEntity<>(restResponse, HttpStatus." + getDefaultResponseStatus(methodSpec.getHttpRequestMethod()) + ");");
        } else {
            methodDeclaration.setType("ResponseEntity<?>");
            methodBody.addStatement("return new ResponseEntity<>(HttpStatus." + getDefaultResponseStatus(methodSpec.getHttpRequestMethod()) + ");");
        }
    }

    private JavaDocument maybeGenerateControllerITestClass(String typeFullName, JavaDocument controllerJava) {
        Pair<JavaDocument, CompilationUnit> docUnit = GenUtility.maybeCreateJavaClass(
                getProjectSpec().getSrcTestJava(), typeFullName, getGenConfig().isForce());
        CompilationUnit cUnit = docUnit.getRight();
        if (cUnit != null) {
            cUnit.addImport("org.junit.jupiter.api.Test");
            cUnit.addImport("org.junit.jupiter.api.extension.ExtendWith");
            cUnit.addImport("org.springframework.beans.factory.annotation.Autowired");
            cUnit.addImport("org.springframework.boot.test.context.SpringBootTest");
            cUnit.addImport("org.springframework.test.context.junit.jupiter.SpringExtension");

            ClassOrInterfaceDeclaration ciDeclaration = (ClassOrInterfaceDeclaration) docUnit.getLeft().getMainTypeDeclaration();
            ciDeclaration.addSingleMemberAnnotation("ExtendWith", "SpringExtension.class");
            ciDeclaration.addAnnotation("SpringBootTest");

            cUnit.addImport(GenUtility.joinPackageName(controllerJava.getPackageName(), controllerJava.getMainTypeDeclaration().getNameAsString()));
            ciDeclaration.addField(controllerJava.getMainTypeDeclaration().getNameAsString(), "controller").addAnnotation("Autowired");
        }
        return docUnit.getLeft();
    }

    private TextFileWrapper maybeGenerateControllerTestClass(String typeFullName, JavaDocument controllerJava) {
        Pair<JavaDocument, CompilationUnit> docUnit = GenUtility.maybeCreateJavaClass(
                getProjectSpec().getSrcTestJava(), typeFullName, getGenConfig().isForce());
        CompilationUnit cUnit = docUnit.getRight();
        if (cUnit != null) {
            cUnit.addImport("com.fasterxml.jackson.databind.ObjectMapper");
            cUnit.addImport("org.junit.jupiter.api.Test");
            cUnit.addImport("org.springframework.beans.factory.annotation.Autowired");
            cUnit.addImport("org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest");
            cUnit.addImport("org.springframework.http.MediaType");
            cUnit.addImport("org.springframework.test.web.servlet.MockMvc");
            cUnit.addImport("org.springframework.test.web.servlet.request.MockMvcRequestBuilders", true, true);
            cUnit.addImport("org.springframework.test.web.servlet.result.MockMvcResultMatchers.status", true, false);

            ClassOrInterfaceDeclaration ciDeclaration = (ClassOrInterfaceDeclaration) docUnit.getLeft().getMainTypeDeclaration();
            ciDeclaration.addSingleMemberAnnotation("WebMvcTest", controllerJava.getMainTypeDeclaration().getNameAsString() + ".class");

            ciDeclaration.addField("MockMvc", "mockMvc").addAnnotation("Autowired");
            ciDeclaration.addField("ObjectMapper", "objectMapper").addAnnotation("Autowired");

        }
        return docUnit.getLeft();
    }

    private JavaDocument maybeGenerateRequestDtoClass(String typeFullName, SpringRestMethodSpec methodSpec) {
        if (BooleanUtils.isNotTrue(methodSpec.getHttpRequestMethod().getHasRequestBody())) {
            return null;
        }
        return SpringDtoGenerator.generateRestDto(getGenCtx(), getProjectSpec().getSrcMainJava(), typeFullName);
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
        return SpringDtoGenerator.generateRestDto(getGenCtx(), getProjectSpec().getSrcMainJava(), typeFullName);
    }

    private JavaDocument maybeGenerateControllerClass(String typeFullName, SpringRestMethodSpec methodSpec) {
        Pair<JavaDocument, CompilationUnit> docUnit = GenUtility.maybeCreateJavaClass(
                getProjectSpec().getSrcMainJava(), typeFullName, getGenConfig().isForce());
        CompilationUnit cUnit = docUnit.getRight();
        if (cUnit != null) {
            cUnit.addImport("lombok.RequiredArgsConstructor");
            cUnit.addImport("lombok.extern.slf4j.Slf4j");
            cUnit.addImport("org.springframework.web.bind.annotation.RequestMapping");
            cUnit.addImport("org.springframework.web.bind.annotation.RestController");

            ClassOrInterfaceDeclaration ciDeclaration = (ClassOrInterfaceDeclaration) docUnit.getLeft().getMainTypeDeclaration();
            ciDeclaration.addAnnotation("RestController");
            ciDeclaration.addSingleMemberAnnotation("RequestMapping", ciDeclaration.getNameAsString() + ".ROOT_URI");
            ciDeclaration.addAnnotation("RequiredArgsConstructor");
            ciDeclaration.addAnnotation("Slf4j");

            String rootUri = "/" + getProjectSpec().getRestApiUriRoot() + "/" + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, methodSpec.getTypeLemma());
            ciDeclaration.addFieldWithInitializer(String.class, "ROOT_URI", new StringLiteralExpr(rootUri), Keyword.STATIC, Keyword.FINAL);
        }
        return docUnit.getLeft();
    }

}
