package io.github.codingspeedup.execdoc.spring.generators.spec.impl;

import io.github.codingspeedup.execdoc.spring.generators.spec.SpringBootProject;
import lombok.Builder;

import java.io.File;

@Builder
public class SpringBootProjectImpl implements SpringBootProject {

    private File rootFolder;
    private String rootPackage;

    @Override
    public File getResourceFile() {
        return rootFolder;
    }

    @Override
    public String getPackageName() {
        return rootPackage;
    }

}
