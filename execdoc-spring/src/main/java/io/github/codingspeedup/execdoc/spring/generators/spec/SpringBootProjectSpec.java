package io.github.codingspeedup.execdoc.spring.generators.spec;

import io.github.codingspeedup.execdoc.generators.spec.MavenProjectSpec;

public interface SpringBootProjectSpec extends MavenProjectSpec {

    String getRestPackageName();

    default String getRestApiRoot() {
        return "api";
    }

}
