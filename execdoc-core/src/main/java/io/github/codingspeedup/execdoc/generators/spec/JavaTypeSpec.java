package io.github.codingspeedup.execdoc.generators.spec;

import org.apache.commons.lang3.StringUtils;

public interface JavaTypeSpec extends ResourceSpec {

    String getPackageName();

    String getTypeName();

    String getTypeLemma();

}
