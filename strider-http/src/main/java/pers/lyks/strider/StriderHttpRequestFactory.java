package pers.lyks.strider;

import pers.lyks.strider.annotation.Method;

import java.io.IOException;

/**
 * @author lawyerance
 * @version 1.0 2019-09-10
 */
@FunctionalInterface
public interface StriderHttpRequestFactory {
    StriderHttpRequest createRequest(String url, Method method) throws IOException;
}
