package io.github.codingspeedup.execdoc.spring.generators.spec;

import io.github.codingspeedup.execdoc.generators.spec.MavenProjectSpec;
import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;

public interface SpringBootProject extends MavenProjectSpec {

    default String getRestApiUriRoot() {
        return "api";
    }

    default String getRestControllerPackageName() {
        return GenUtility.joinPackageName(getPackageName(), "web.controller");
    }

    default String getRestIntegrationTestsPackageName() {
        return GenUtility.joinPackageName(getPackageName(), "integration.rest");
    }

    default String getServicesPackageName() {
        return GenUtility.joinPackageName(getPackageName(), "services");
    }

    default String getServicesImplPackageName() {
        return GenUtility.joinPackageName(getPackageName(), "services.impl");
    }

}
