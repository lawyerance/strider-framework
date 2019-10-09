package pers.lyks.strider.binding;


import pers.lyks.strider.RequestMetaData;
import pers.lyks.strider.StriderHttpRequestFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class RequestProxyFactory<T> {

    private final Class<T> mapperInterface;
    private final Map<Method, RequestMetaData> methodCache = new ConcurrentHashMap<>();

    public RequestProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public Class<T> getMapperInterface() {
        return mapperInterface;
    }

    public Map<Method, RequestMetaData> getMethodCache() {
        return methodCache;
    }

    @SuppressWarnings("unchecked")
    protected T newInstance(RequestProxy<T> requestProxy) {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, requestProxy);
    }

    public T newInstance(StriderHttpRequestFactory requestFactory) {
        final RequestProxy<T> requestProxy = new RequestProxy(mapperInterface, methodCache, requestFactory);
        return newInstance(requestProxy);
    }

}
