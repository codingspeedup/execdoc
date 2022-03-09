package io.github.codingspeedup.execdoc.spring.generators.spec;

import io.github.codingspeedup.execdoc.generators.spec.MavenProjectSpec;
import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;

public interface SpringBootProjectSpec extends MavenProjectSpec {

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

    default String getDomainPackageName() {
        return GenUtility.joinPackageName(getPackageName(), "domain");
    }

    default String getEntityPackageName() {
        return GenUtility.joinPackageName(getPackageName(), "entities");
    }

    default String getFiniteDomainsPackageName() {
        return GenUtility.joinPackageName(getDomainPackageName(), "finite");
    }

}
