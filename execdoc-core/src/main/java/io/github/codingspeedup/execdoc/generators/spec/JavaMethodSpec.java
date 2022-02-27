package io.github.codingspeedup.execdoc.generators.spec;

public interface JavaMethodSpec extends JavaTypeSpec {

    String getMethodName();

    default String getDtoPackageName() {
        throw new UnsupportedOperationException();
    }

    default String getDtoInputTypeSimpleName() {
        throw new UnsupportedOperationException();
    }

    default String getDtoOutputTypeSimpleName() {
        throw new UnsupportedOperationException();
    }

}
