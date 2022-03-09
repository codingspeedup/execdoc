package io.github.codingspeedup.execdoc.spring.generators.spec.impl;

import io.github.codingspeedup.execdoc.spring.generators.spec.SpringServiceMethodSpec;
import lombok.Builder;

@Builder
public class SpringServiceMethodImpl implements SpringServiceMethodSpec {

    private String packageName;
    private String typeLemma;
    private String methodLemma;

    @Override
    public String getMethodName() {
        return methodLemma;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public String getTypeName() {
        return typeLemma + "Service";
    }

    @Override
    public String getTypeLemma() {
        return typeLemma;
    }

}
