package io.github.codingspeedup.execdoc.spring.generators.spec;

import com.google.common.base.CaseFormat;
import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;

import java.util.Locale;

public interface SpringRestMethodSpec extends SpringComponentMethodSpec {

    /**
     * https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods
     */
    HttpRequestMethod getHttpRequestMethod();

    default String getDtoPackageName() {
        return GenUtility.joinPackageName(getPackageName(), "dto", getTypeLemma().toLowerCase(Locale.ROOT));
    }

    @Override
    default String getDtoInputTypeSimpleName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, getMethodName()) + "RestRequest";
    }

    @Override
    default String getDtoOutputTypeSimpleName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, getMethodName()) + "RestResponse";
    }

}
