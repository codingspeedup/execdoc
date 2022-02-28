package io.github.codingspeedup.execdoc.spring.blueprint;

import io.github.codingspeedup.execdoc.blueprint.Blueprint;

import java.io.File;

public class SpringBlueprint extends Blueprint<SpringMaster> {

    public SpringBlueprint(File repository) {
        super(SpringMaster.class, repository);
    }

}
