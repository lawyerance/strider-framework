package pers.lyks.strider.util;

import lombok.extern.slf4j.Slf4j;
import pers.lyks.strider.resource.Resources;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author lawyerance
 * @version 1.0 2019-08-29
 */
@Slf4j
public class ClassScanner {
    private static final char PACKAGE_SEPARATOR = '.';

    private static final char PATH_SEPARATOR = '/';

    private static final String CLASS_SUFFIX = ".class";
    private static final String FILE_PROTOCOL = "file";

    private static final String JAR_PROTOCOL = "jar";


    public static Set<Class<?>> getTypesAnnotatedWith(Annotation annotation, String... packages) {
        return scan(null, Class::isAnnotation, true, packages);
    }

    public static Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> superType, String... packages) {
        return scan(null, (clazz) -> clazz.isAnnotationPresent(superType), true, packages);
    }

    /**
     * Scan classes under multiple package paths.
     *
     * @param classLoader The scan class loader.
     * @param predicate   The predicate (boolean-valued function) of class argument
     * @param recursive   Whether scan recursive.
     *                    <p>If true scan current package and children packages, otherwise only current package.</p>
     * @param packages    The scan array of package.
     * @return The set collection of class.
     */
    public static Set<Class<?>> scan(ClassLoader classLoader, Predicate<Class<?>> predicate, boolean recursive, String... packages) {
        Set<Class<?>> result = new HashSet<>();
        for (String packageName : packages) {
            try {
                Set<Class<?>> set = doScan(classLoader, packageName, predicate, recursive);
                result.addAll(set);
            } catch (IOException e) {
                logger.error("scan class error with package name: " + packageName, e);
            }
        }
        return result;
    }

    private static Set<Class<?>> doScan(ClassLoader classLoader, String packageName, Predicate<Class<?>> predicate, boolean recursive) throws IOException {
        Set<Class<?>> result = new HashSet<>();
        String packageDirName = packageName.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);

        Enumeration<URL> resources = Resources.getResources(packageDirName, classLoader);
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            String protocol = url.getProtocol();
            if (FILE_PROTOCOL.equals(protocol)) {
                String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.name());
                findClassByFile(classLoader, packageName, new File(filePath), predicate, recursive, result);
            } else if (JAR_PROTOCOL.equals(protocol)) {
                JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                findClassByJar(classLoader, packageDirName, jarFile, predicate, recursive, result);
            }
        }

        return result;
    }

    private static void findClassByFile(ClassLoader classLoader, String packageName, File file, Predicate<Class<?>> predicate, boolean recursive, Set<Class<?>> result) {
        if (!file.exists() || !file.isDirectory()) {
            return;
        }
        File[] files = file.listFiles((f) -> f.isDirectory() || f.getName().endsWith(CLASS_SUFFIX));
        if (null == files || files.length <= 0) {
            return;
        }
        String className;
        for (File f : files) {
            className = f.getName();
            if (f.isDirectory()) {
                if (recursive) {
                    findClassByFile(classLoader, packageName + PACKAGE_SEPARATOR + className, f, predicate, true, result);
                }
                continue;
            }
            Optional<Class<?>> classOptional = loadClass(packageName + PACKAGE_SEPARATOR + className.substring(0, className.lastIndexOf(CLASS_SUFFIX)), classLoader);
            classOptional.filter(predicate).ifPresent(result::add);
        }
    }

    private static void findClassByJar(ClassLoader classLoader, String packagePath, JarFile jarFile, Predicate<Class<?>> predicate, boolean recursive, Set<Class<?>> result) {
        Enumeration<JarEntry> entries = jarFile.entries();
        JarEntry jarEntry;
        while (entries.hasMoreElements()) {
            jarEntry = entries.nextElement();
            String name = prefixTrim(jarEntry.getName(), PACKAGE_SEPARATOR);
            if (jarEntry.isDirectory() || !name.endsWith(CLASS_SUFFIX)) {
                // not package path and not class file
                return;
            }
            if (name.startsWith(packagePath)) {
                String suffix = name.substring(packagePath.length());
                if (recursive || suffix.lastIndexOf(PATH_SEPARATOR) < 1) {
                    //remove '.class' character last, and then transform path separator to package separator
                    String fullName = name.substring(0, name.length() - CLASS_SUFFIX.length());
                    Optional<Class<?>> classOptional = loadClass(fullName.replace(PATH_SEPARATOR, PACKAGE_SEPARATOR), classLoader);
                    classOptional.filter(predicate).ifPresent(result::add);
                }
            }
        }
    }


    private static Optional<Class<?>> loadClass(String fullName, ClassLoader classLoader) {
        String className = prefixTrim(fullName, PACKAGE_SEPARATOR);
        /* ignore module info class since jdk 9 */
        if ("module-info".equals(className)) {
            return Optional.empty();
        }
        Class<?> clazz = null;
        try {
            clazz = Resources.classForName(className, classLoader);
        } catch (ClassNotFoundException e) {
            logger.error("load class error with {}", className, e);
        }
        return Optional.ofNullable(clazz);
    }

    private static String prefixTrim(String content, char c) {
        if (null == content || content.length() <= 1) {
            return content;
        }
        if (content.charAt(0) == c) {
            return content.substring(1);
        }
        return content;
    }
}
