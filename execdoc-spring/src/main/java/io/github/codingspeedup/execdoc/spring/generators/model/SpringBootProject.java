package io.github.codingspeedup.execdoc.spring.generators.model;

import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;
import io.github.codingspeedup.execdoc.spring.generators.spec.SpringBootProjectSpec;
import lombok.Builder;

import java.io.File;

@Builder
public class SpringBootProject implements SpringBootProjectSpec {

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

    @Override
    public String getRestPackageName() {
        return GenUtility.joinPackageName(getPackageName(), "web.rest");
    }

}
