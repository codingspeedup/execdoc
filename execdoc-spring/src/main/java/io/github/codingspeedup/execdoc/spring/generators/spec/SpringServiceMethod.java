package io.github.codingspeedup.execdoc.spring.generators.spec;

import com.google.common.base.CaseFormat;
import io.github.codingspeedup.execdoc.generators.spec.JavaMethodSpec;

public interface SpringServiceMethod extends JavaMethodSpec {

    @Override
    default String getDtoInputTypeName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, getMethodName()) + "Args";
    }

    @Override
    default String getDtoOutputTypeName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, getMethodName()) + "Result";
    }

    default String getImplementingTypeName() {
        return getTypeName() + "Impl";
    }

}
