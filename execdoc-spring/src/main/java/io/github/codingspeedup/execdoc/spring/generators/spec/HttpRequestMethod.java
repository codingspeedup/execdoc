package io.github.codingspeedup.execdoc.spring.generators.spec;

import lombok.Getter;

public enum HttpRequestMethod {

    DELETE(false, true, false, false, true),
    GET(true, true, true, false, true),
    PATCH(false, false, false, true, true),
    POST(false, false, false, true, true),
    PUT(false, true, false, true, true),

    ;

    @Getter
    private final boolean safe;
    @Getter
    private final boolean idempotent;
    @Getter
    private final boolean cacheable;
    @Getter
    private final Boolean hasRequestBody;
    @Getter
    private final Boolean hasResponseBody;

    HttpRequestMethod(boolean safe, boolean idempotent, boolean cacheable, Boolean requestBody, Boolean responseBody) {
        this.safe = safe;
        this.idempotent = idempotent;
        this.cacheable = cacheable;
        this.hasRequestBody = requestBody;
        this.hasResponseBody = responseBody;
    }

}
