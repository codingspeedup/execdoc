package io.github.codingspeedup.execdoc.spring.generators.spec;

import io.github.codingspeedup.execdoc.generators.spec.MavenProjectSpec;
import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;

public interface SpringBootProjectSpec extends MavenProjectSpec {

    String getRestPackageName();

    default String getRestApiRoot() {
        return "api";
    }

    default String getIntegrationTestsPackageName() {
        return GenUtility.joinPackageName(getPackageName(), "itest.rest");
    }

}
