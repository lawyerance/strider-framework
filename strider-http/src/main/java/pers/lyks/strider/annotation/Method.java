package pers.lyks.strider.annotation;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lawyerance
 * @version 1.0 2019-08-30
 */
public enum Method {
    GET(false),
    POST(true),
    PUT(true),
    DELETE(false),
    PATCH(true),
    HEAD(false),
    OPTIONS(false),
    TRACE(false);

    private final boolean hasBody;

    Method(boolean hasBody) {
        this.hasBody = hasBody;
    }

    /**
     * Check if this HTTP method has/needs a request body
     *
     * @return if body needed
     */
    public final boolean hasBody() {
        return hasBody;
    }


    public static Method nameOf(String name) {
        for (Method method : values()) {
            if (method.name().equalsIgnoreCase(name)) {
                return method;
            }
        }
        throw new IllegalArgumentException(String.format("http method may be not null or out of method range: [ %s ], with method name: %s.", valueChain(), name));
    }

    public static String valueChain() {
        return Stream.of(values()).map(Method::name).collect(Collectors.joining(", "));
    }
}
