package io.github.codingspeedup.execdoc.generators.spec;

import org.apache.commons.lang3.StringUtils;

public interface JavaTypeSpec extends ResourceSpec {

    default String getTypeFullName() {
        if (StringUtils.isNotBlank(getPackageName())) {
            return getPackageName() + "." + getTypeSimpleName();
        }
        return getTypeSimpleName();
    }

    String getPackageName();

    String getTypeSimpleName();

    String getTypeLemma();

}
