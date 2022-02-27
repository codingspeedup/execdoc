package io.github.codingspeedup.execdoc.generators.utilities;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

import static com.github.javaparser.ast.Modifier.Keyword;

public class GenUtility {

    public static String toBasicL10nKey(String label) {
        label = StringUtils.stripAccents(label).toLowerCase(Locale.ROOT);
        label = label.replaceAll("[^a-z0-9\\s]", "");
        label = label.trim().replaceAll("\\s+", "-");
        int maxLength = 76;
        if (label.length() > maxLength) {
            if (label.charAt(maxLength - 1) == '-') {
                --maxLength;
            }
            label = label.substring(0, maxLength);
        }
        return label;
    }


    public static String simpleQuote(String string) {
        if (string == null) {
            return null;
        }
        if (string.contains("'")) {
            throw new UnsupportedOperationException("Escaping \"'\" is not specified");
        }
        return "'" + string + "'";
    }

    public static String joinPackageName(String... parts) {
        StringBuilder packageNameBuilder = new StringBuilder();
        if (ArrayUtils.isNotEmpty(parts)) {
            for (String part : parts) {
                packageNameBuilder.append(StringUtils.trimToEmpty(part)).append(".");
            }
        }
        String packageName = packageNameBuilder.toString().replaceAll("\\.+", ".");
        if (packageName.startsWith(".")) {
            packageName = packageName.substring(1);
        }
        if (packageName.endsWith(".")) {
            packageName = packageName.substring(0, packageName.length() - 1);
        }
        return packageName;
    }

    public static File fileOf(File root, String packageName, String fileName) {
        if (StringUtils.isNotBlank(packageName)) {
            root = new File(root, packageName.replaceAll("\\.", "/"));
        }
        return new File(root, fileName);
    }

    @SneakyThrows
    public static String toString(List<TextFileWrapper> textFiles) {
        StringBuilder sb = new StringBuilder();
        for (TextFileWrapper sourceFile : textFiles) {
            String filePath = sourceFile.getWrappedFile().getCanonicalPath();
            String fileName = sourceFile.getWrappedFile().getName();
            sb.append("\n");
            int pathLength = filePath.length() - fileName.length();
            IntStream.range(0, pathLength + 1).forEach(i -> sb.append("="));
            sb.append("\n").append(filePath).append("\n");
            IntStream.range(0, pathLength - 1).forEach(i -> sb.append("="));
            IntStream.range(0, fileName.length() + 1).forEach(i -> sb.append("-"));
            sb.append("\n").append(sourceFile);
            IntStream.range(0, filePath.length()).forEach(i -> sb.append("-"));
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void makeSerializable(ClassOrInterfaceDeclaration ciDeclaration) {
        ciDeclaration.addImplementedType(Serializable.class);
        ciDeclaration.addFieldWithInitializer(long.class, "serialVersionUID", new LongLiteralExpr("1L"),
                Keyword.PRIVATE, Keyword.STATIC, Keyword.FINAL);
    }

    public static void addLombokGettersAndSetters(ClassOrInterfaceDeclaration ciDeclaration) {
        CompilationUnit cUnit = (CompilationUnit) ciDeclaration.getParentNode().orElseThrow();
        cUnit.addImport("lombok.Getter");
        cUnit.addImport("lombok.NoArgsConstructor");
        cUnit.addImport("lombok.Setter");
        ciDeclaration.addAnnotation("NoArgsConstructor");
        ciDeclaration.addAnnotation("Getter");
        ciDeclaration.addAnnotation("Setter");
    }

}
