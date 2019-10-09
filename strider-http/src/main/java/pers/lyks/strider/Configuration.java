package pers.lyks.strider;

import pers.lyks.strider.binding.RequestRegistry;
import pers.lyks.strider.simple.DefaultStriderHttpRequestFactory;

import java.util.stream.Stream;

/**
 * @author lawyerance
 * @version 1.0 2019-09-03
 */
public class Configuration {
    private final RequestRegistry requestRegistry;
    private StriderHttpRequestFactory requestFactory = new DefaultStriderHttpRequestFactory();

    public Configuration() {
        this.requestRegistry = new RequestRegistry(this);
    }

    public void scan(String... basePackages) {
        Stream.of(basePackages).distinct().forEach(requestRegistry::addRequests);
    }

    public RequestRegistry getRequestRegistry() {
        return requestRegistry;
    }

    public StriderHttpRequestFactory getRequestFactory() {
        return this.requestFactory;
    }
}
