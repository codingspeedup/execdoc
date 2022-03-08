package io.github.codingspeedup.execdoc.spring.generators.spec;

import io.github.codingspeedup.execdoc.generators.spec.JavaTypeSpec;

public interface SpringEnumConstant extends JavaTypeSpec {

    String getConstantName();

    String getConstantDescription();

}
