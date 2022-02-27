package io.github.codingspeedup.execdoc.spring.generators.spec;

public interface SpringRestMethodSpec extends SpringComponentMethodSpec {

    /**
     * https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods
     */
    HttpRequestMethod getHttpRequestMethod();

}
