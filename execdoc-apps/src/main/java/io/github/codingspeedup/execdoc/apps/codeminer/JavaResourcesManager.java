package io.github.codingspeedup.execdoc.apps.codeminer;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import io.github.codingspeedup.execdoc.apps.codeminer.clipboard.CodeMinerClipboard;
import io.github.codingspeedup.execdoc.apps.codeminer.clipboard.JavaMethodsSheet;
import io.github.codingspeedup.execdoc.apps.codeminer.clipboard.JavaPackagesSheet;
import io.github.codingspeedup.execdoc.apps.codeminer.clipboard.JavaTypesSheet;
import io.github.codingspeedup.execdoc.miners.resources.java.JavaFilesMiner;
import io.github.codingspeedup.execdoc.miners.resources.java.workflows.CompilationUnitParser;
import io.github.codingspeedup.execdoc.miners.resources.java.workflows.JavaMinerState;
import io.github.codingspeedup.execdoc.miners.resources.java.workflows.JavaTypeFinder;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaParserUtility;
import io.github.codingspeedup.execdoc.toolbox.resources.ResourceGroup;
import io.github.codingspeedup.execdoc.toolbox.resources.filesystem.FileOrFolderResource;
import io.github.codingspeedup.execdoc.toolbox.resources.filesystem.FileResource;
import io.github.codingspeedup.execdoc.toolbox.resources.java.JavaMethodResource;
import io.github.codingspeedup.execdoc.toolbox.resources.java.JavaPackageResource;
import io.github.codingspeedup.execdoc.toolbox.resources.java.JavaTypeResource;
import io.github.codingspeedup.execdoc.toolbox.workflow.Pipeline;
import io.github.codingspeedup.execdoc.toolbox.workflow.Workflow;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JavaResourcesManager {

    private final CodeMinerClipboard clipboard;

    public JavaResourcesManager(CodeMinerClipboard clipboard) {
        this.clipboard = clipboard;
    }

    @SneakyThrows
    public List<JavaTypeResource> explore(FileOrFolderResource group) {
        Cell groupCell = clipboard.getResourcesSheet().selectByDescriptor(group.getDescriptor());
        JavaTypesSheet typesSheet = clipboard.getJavaTypesSheet();
        List<JavaTypeResource> selection = typesSheet.getResourcesByGroup(groupCell);
        if (CollectionUtils.isEmpty(selection)) {
            new FileResourcesManager(clipboard).explore(group);

            JavaPackagesSheet packagesSheet = clipboard.getJavaPackagesSheet();
            Set<String> packages = new HashSet<>();

            JavaMethodsSheet methodsSheet = clipboard.getJavaMethodsSheet();

            JavaMinerState state = new JavaMinerState();

            Workflow<JavaMinerState, File, List<JavaTypeResource>> wf = new Workflow<>(state,
                    new Pipeline<>(state, new CompilationUnitParser())
                            .extend(new JavaTypeFinder()));

            ResourceGroup rGroup = ResourceGroup.builder().res(group).build();
            JavaFilesMiner miner = new JavaFilesMiner(rGroup, resource -> {
                if (resource instanceof FileResource) {
                    List<JavaTypeResource> typeResources = wf.process(((FileResource) resource).getFile());
                    packages.add(wf.getSharedState().getJDoc().getPackageName());
                    for (JavaTypeResource typeResource : typeResources) {
                        System.out.println(typeResource.getDescription());
                        Cell fileCell = clipboard.getFilesSheet().selectByDescriptor(groupCell, resource.getDescriptor());
                        Row typeRow = typesSheet.insert(fileCell, typeResource);
                        if (typeResource.getDeclaration() instanceof ClassOrInterfaceDeclaration) {
                            fileCell = typeRow.getCell(JavaTypesSheet.DESCRIPTOR_COL_IDX);
                            ClassOrInterfaceDeclaration ciDeclaration = (ClassOrInterfaceDeclaration) typeResource.getDeclaration();
                            for (MethodDeclaration mDecl : ciDeclaration.getMethods()) {
                                String description = mDecl.getNameAsString() + "(" + JavaParserUtility.buildMethodSignature(mDecl) + ")";
                                JavaMethodResource methodResource = new JavaMethodResource(description);
                                methodResource.setPublic(mDecl.isPublic());
                                methodsSheet.insert(fileCell, methodResource);
                            }
                        }
                        selection.add(typeResource);
                    }
                }
            });
            miner.scan(null);

            for (String pkg : packages) {
                packagesSheet.insert(null, new JavaPackageResource(pkg));
            }

            packagesSheet.normalize();
            typesSheet.normalize();
            methodsSheet.normalize();
        }
        return selection;
    }

}
