package com.gmlimsqi.framework.aspectj;

public class SapLogContext {
    // 使用 InheritableThreadLocal 支持线程池场景
    private static final InheritableThreadLocal<String> REQUEST_BODY = new InheritableThreadLocal<>();
    private static final InheritableThreadLocal<String> RESPONSE_BODY = new InheritableThreadLocal<>();

    public static void setRequestBody(String body) {
        REQUEST_BODY.set(body);
    }

    public static void setResponseBody(String body) {
        RESPONSE_BODY.set(body);
    }

    public static String getRequestBody() {
        return REQUEST_BODY.get();
    }

    public static String getResponseBody() {
        return RESPONSE_BODY.get();
    }

    public static void clear() {
        REQUEST_BODY.remove();
        RESPONSE_BODY.remove();
    }
}
