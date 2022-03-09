package io.github.codingspeedup.execdoc.spring.generators.spec;

import com.google.common.base.CaseFormat;
import io.github.codingspeedup.execdoc.generators.spec.JavaMethodSpec;

public interface SpringRestMethodSpec extends JavaMethodSpec {

    /**
     * https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods
     */
    HttpRequestMethod getHttpRequestMethod();

    @Override
    default String getDtoInputTypeName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, getMethodName()) + "Request";
    }

    @Override
    default String getDtoOutputTypeName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, getMethodName()) + "Response";
    }

}
