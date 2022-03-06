package io.github.codingspeedup.execdoc.generators.spec;

import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;

import java.util.Locale;

public interface JavaMethodSpec extends JavaTypeSpec {

    String getMethodName();

    default String getDtoPackageName() {
        return GenUtility.joinPackageName(getPackageName(), "dto", getTypeLemma().toLowerCase(Locale.ROOT));
    }

    default String getDtoInputTypeName() {
        throw new UnsupportedOperationException();
    }

    default String getDtoOutputTypeName() {
        throw new UnsupportedOperationException();
    }



}
