package pers.lyks.strider.binding;

import pers.lyks.strider.MethodMetadata;
import pers.lyks.strider.StriderHttpRequestFactory;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;


public class RequestProxy<T> implements InvocationHandler, Serializable {

    private final Class<T> mapperInterface;
    private final Map<Method, MethodMetadata> methodCache;
    private final StriderHttpRequestFactory requestFactory;

    public RequestProxy(Class<T> mapperInterface, Map<Method, MethodMetadata> methodCache, StriderHttpRequestFactory requestFactory) {
        this.mapperInterface = mapperInterface;
        this.methodCache = methodCache;
        this.requestFactory = requestFactory;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            } else if (isDefaultMethod(method)) {
                return invokeDefaultMethod(proxy, method, args);
            }
        } catch (Throwable t) {
            throw new RuntimeException();
        }
        final MethodMetadata methodMetadata = cachedMapperMethod(method);
        return methodMetadata.execute(args);
    }

    private MethodMetadata cachedMapperMethod(Method method) {
        MethodMetadata methodMetadata = methodCache.get(method);
        if (methodMetadata == null) {
            methodMetadata = new MethodMetadata(mapperInterface, method, requestFactory);
            methodMetadata.resolver();
            methodCache.put(method, methodMetadata);
        }
        return methodMetadata;
    }

    private Object invokeDefaultMethod(Object proxy, Method method, Object[] args)
            throws Throwable {
        final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
                .getDeclaredConstructor(Class.class, int.class);
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        final Class<?> declaringClass = method.getDeclaringClass();
        return constructor
                .newInstance(declaringClass,
                        MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED
                                | MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC)
                .unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(args);
    }

    /**
     * Backport of java.lang.reflect.ExecutorMethod#isDefault()
     */
    private boolean isDefaultMethod(Method method) {
        return ((method.getModifiers()
                & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) == Modifier.PUBLIC)
                && method.getDeclaringClass().isInterface();
    }
}
