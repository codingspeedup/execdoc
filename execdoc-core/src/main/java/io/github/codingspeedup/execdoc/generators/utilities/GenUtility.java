package io.github.codingspeedup.execdoc.generators.utilities;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaDocument;
import io.github.codingspeedup.execdoc.toolbox.files.Folder;
import io.github.codingspeedup.execdoc.toolbox.utilities.DateTimeUtility;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

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

    public static void addCreationJavadoc(TypeDeclaration<?> tDeclaration) {
        tDeclaration.setJavadocComment("Created on " + DateTimeUtility.toIsoDateString(LocalDate.now()));
    }

    public static String[] splitTypeFullName(String typeFullName) {
        int lastDotIndex = typeFullName.lastIndexOf(".");
        if (lastDotIndex < 0) {
            return new String[]{"", typeFullName};
        }
        return new String[]{typeFullName.substring(0, lastDotIndex), typeFullName.substring(lastDotIndex + 1)};
    }

    public static Pair<JavaDocument, CompilationUnit> maybeCreateJavaClass(Folder srcFolder, String typeFullName, boolean override) {
        return maybeCreateJavaClass(0, srcFolder, typeFullName, override);
    }

    public static Pair<JavaDocument, CompilationUnit> maybeCreateJavaInterface(Folder srcFolder, String typeFullName, boolean override) {
        return maybeCreateJavaClass(1, srcFolder, typeFullName, override);
    }

    public static Pair<JavaDocument, CompilationUnit> maybeCreateJavaEnum(Folder srcFolder, String typeFullName, boolean override) {
        return maybeCreateJavaClass(2, srcFolder, typeFullName, override);
    }

    private static Pair<JavaDocument, CompilationUnit> maybeCreateJavaClass(int javaType, Folder srcFolder, String typeFullName, boolean override) {
        String[] packageType = splitTypeFullName(typeFullName);
        File javaFile = fileOf(srcFolder, packageType[0], packageType[1] + ".java");
        JavaDocument javaDocument = new JavaDocument(javaFile);
        CompilationUnit cUnit = null;
        if (!javaFile.exists()) {
            cUnit = javaDocument.getCompilationUnit();
        } else if (override) {
            javaDocument.setCompilationUnit(new CompilationUnit());
            cUnit = javaDocument.getCompilationUnit();
        }
        if (cUnit != null) {
            cUnit.setPackageDeclaration(packageType[0]);
            switch (javaType) {
                case 0: {
                    ClassOrInterfaceDeclaration ciDeclaration = cUnit.addClass(packageType[1], Modifier.Keyword.PUBLIC);
                    addCreationJavadoc(ciDeclaration);
                    break;
                }
                case 1: {
                    ClassOrInterfaceDeclaration ciDeclaration = cUnit.addInterface(packageType[1], Modifier.Keyword.PUBLIC);
                    addCreationJavadoc(ciDeclaration);
                    break;
                }
                case 2: {
                    EnumDeclaration eDeclaration = cUnit.addEnum(packageType[1], Modifier.Keyword.PUBLIC);
                    addCreationJavadoc(eDeclaration);
                    break;
                }
            }
        }
        return Pair.of(javaDocument, cUnit);
    }


}
