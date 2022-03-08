package io.github.codingspeedup.execdoc.spring.generators.spec.impl;

import io.github.codingspeedup.execdoc.spring.generators.spec.SpringEnumConstant;
import lombok.Builder;

@Builder
public class SpringEnumConstantImpl implements SpringEnumConstant {

    private String packageName;
    private String typeLemma;
    private String constantName;
    private String constantDescription;

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public String getTypeName() {
        return typeLemma;
    }

    @Override
    public String getTypeLemma() {
        return typeLemma;
    }

    @Override
    public String getConstantName() {
        return constantName;
    }

    @Override
    public String getConstantDescription() {
        return constantDescription;
    }

}
