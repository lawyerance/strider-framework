package pers.lyks.strider.resource;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author lawyerance
 * @version 1.0 2019-08-29
 */
class ClassLoaderWrapper {
    private ClassLoader systemClassLoader;
    private ClassLoader defaultClassLoader;

    ClassLoaderWrapper() {
        try {
            systemClassLoader = ClassLoader.getSystemClassLoader();
        } catch (SecurityException ignore) {
        }
    }

     Enumeration<URL> getResources(String resource) {
        return getResources(resource, getClassLoaders(null));
    }

     Enumeration<URL> getResources(String resource, ClassLoader classLoader) {
        return getResources(resource, getClassLoaders(classLoader));
    }

    Class<?> classForName(String name) throws ClassNotFoundException {
        return classForName(name, getClassLoaders(null));
    }


    Class<?> classForName(String name, ClassLoader classLoader) throws ClassNotFoundException {
        return classForName(name, getClassLoaders(classLoader));
    }

    private Enumeration<URL> getResources(String resource, ClassLoader[] classLoader) {
        Enumeration<URL> urls = null;
        for (ClassLoader cl : classLoader) {
            if (null != cl) {
                try {
                    urls = cl.getResources(resource);
                    if (null == urls) {
                        urls = cl.getResources("/" + resource);
                    }
                    if (null != urls) {
                        return urls;
                    }
                } catch (IOException ignore) {
                }
            }
        }
        return urls;
    }

    private Class<?> classForName(String name, ClassLoader[] classLoader) throws ClassNotFoundException {
        for (ClassLoader cl : classLoader) {
            if (null != cl) {
                try {
                    Class<?> c = Class.forName(name, true, cl);
                    if (null != c) {
                        return c;
                    }
                } catch (ClassNotFoundException ignore) {
                }
            }
        }
        throw new ClassNotFoundException("Cannot find class: " + name);
    }

    ClassLoader[] getClassLoaders(ClassLoader classLoader) {
        return new ClassLoader[]{
                classLoader,
                defaultClassLoader,
                Thread.currentThread().getContextClassLoader(),
                getClass().getClassLoader(),
                systemClassLoader};
    }
}
