package io.github.codingspeedup.execdoc.spring.generators.spec;

import com.google.common.base.CaseFormat;
import io.github.codingspeedup.execdoc.generators.spec.JavaMethodSpec;
import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;
import io.github.codingspeedup.execdoc.spring.generators.model.HttpRequestMethod;

import java.util.Locale;

public interface SpringRestMethodSpec extends JavaMethodSpec {

    /**
     * https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods
     */
    HttpRequestMethod getHttpRequestMethod();

    default String getDtoPackageName() {
        return GenUtility.joinPackageName(getPackageName(), "dto", getTypeLemma().toLowerCase(Locale.ROOT));
    }

    @Override
    default String getDtoInputTypeName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, getMethodName()) + "Request";
    }

    @Override
    default String getDtoOutputTypeName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, getMethodName()) + "Response";
    }

}
