package io.github.codingspeedup.execdoc.spring.generators.spec.impl;

import io.github.codingspeedup.execdoc.spring.generators.spec.HttpRequestMethod;
import io.github.codingspeedup.execdoc.spring.generators.spec.SpringRestMethodSpec;
import lombok.Builder;


@Builder
public class SpringRestMethodImpl implements SpringRestMethodSpec {

    private String packageName;
    private String typeLemma;
    private String methodLemma;
    private HttpRequestMethod httpMethod;

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
        return typeLemma + "Controller";
    }

    @Override
    public String getTypeLemma() {
        return typeLemma;
    }

    @Override
    public HttpRequestMethod getHttpRequestMethod() {
        return httpMethod;
    }

}
