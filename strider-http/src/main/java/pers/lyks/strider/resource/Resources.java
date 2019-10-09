package pers.lyks.strider.resource;

import java.net.URL;
import java.util.Enumeration;

/**
 * @author lawyerance
 * @version 1.0 2019-08-29
 */
public final class Resources {
    static ClassLoaderWrapper classLoaderWrapper = new ClassLoaderWrapper();

    public static Enumeration<URL> getResources(String resource) {
        return getResources(resource, null);
    }

    public static Enumeration<URL> getResources(String resource, ClassLoader classLoader) {
        return classLoaderWrapper.getResources(resource, classLoader);
    }

    public static Class<?> classForName(String className) throws ClassNotFoundException {
        return classForName(className, null);
    }

    public static Class<?> classForName(String className, ClassLoader classLoader) throws ClassNotFoundException {
        return classLoaderWrapper.classForName(className, classLoader);
    }
}
