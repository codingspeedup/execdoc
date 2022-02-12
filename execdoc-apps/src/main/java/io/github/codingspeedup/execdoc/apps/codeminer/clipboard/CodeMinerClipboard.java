package io.github.codingspeedup.execdoc.apps.codeminer.clipboard;

import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxDocument;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

public class CodeMinerClipboard extends XlsxDocument {

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private boolean dirty;

    public CodeMinerClipboard(File file) {
        super(file);
        int sheetNo = getWorkbook().getNumberOfSheets();
        maybeMakeSheet(ResourcesSheet.NAME);
        dirty = sheetNo < getWorkbook().getNumberOfSheets();
    }

    public void invalidateCache() {
        removeSheet(JavaMethodsSheet.NAME);
        removeSheet(JavaTypesSheet.NAME);
        removeSheet(JavaPackagesSheet.NAME);
        removeSheet(FilesSheet.NAME);
    }

    public ResourcesSheet getResourcesSheet() {
        return new ResourcesSheet(this, getWorkbook().getSheet(ResourcesSheet.NAME));
    }

    public FilesSheet getFilesSheet() {
        return new FilesSheet(this, maybeMakeSheet(FilesSheet.NAME));
    }

    public JavaTypesSheet getJavaTypesSheet() {
        getJavaPackagesSheet();
        return new JavaTypesSheet(this, maybeMakeSheet(JavaTypesSheet.NAME));
    }

    public JavaPackagesSheet getJavaPackagesSheet() {
        getFilesSheet();
        return new JavaPackagesSheet(this, maybeMakeSheet(JavaPackagesSheet.NAME));
    }

    public JavaMethodsSheet getJavaMethodsSheet() {
        getJavaTypesSheet();
        return new JavaMethodsSheet(this, maybeMakeSheet(JavaMethodsSheet.NAME));
    }

}
