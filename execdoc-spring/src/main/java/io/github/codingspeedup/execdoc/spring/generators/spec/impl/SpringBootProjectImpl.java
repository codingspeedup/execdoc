package io.github.codingspeedup.execdoc.spring.generators.spec.impl;

import io.github.codingspeedup.execdoc.spring.generators.spec.SpringBootProjectSpec;
import lombok.Builder;

import java.io.File;

@Builder
public class SpringBootProjectImpl implements SpringBootProjectSpec {

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
