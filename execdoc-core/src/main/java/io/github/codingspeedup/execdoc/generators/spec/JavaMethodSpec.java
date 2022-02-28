package io.github.codingspeedup.execdoc.generators.spec;

public interface JavaMethodSpec extends JavaTypeSpec {

    String getMethodName();

    default String getDtoPackageName() {
        throw new UnsupportedOperationException();
    }

    default String getDtoInputTypeName() {
        throw new UnsupportedOperationException();
    }

    default String getDtoOutputTypeName() {
        throw new UnsupportedOperationException();
    }

}
