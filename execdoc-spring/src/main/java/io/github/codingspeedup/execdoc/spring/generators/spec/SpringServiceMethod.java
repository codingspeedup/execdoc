package io.github.codingspeedup.execdoc.spring.generators.spec;

import com.google.common.base.CaseFormat;
import io.github.codingspeedup.execdoc.generators.spec.JavaMethodSpec;
import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;

import java.util.Locale;

public interface SpringServiceMethod extends JavaMethodSpec {

    default String getImplementationPackageName() {
        return GenUtility.joinPackageName(getPackageName(), "impl");
    }

    default String getImplementingTypeName() {
        return getTypeName() + "Impl";
    }

    @Override
    default String getDtoInputTypeName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, getMethodName()) + "Args";
    }

    @Override
    default String getDtoOutputTypeName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, getMethodName()) + "Result";
    }



}
