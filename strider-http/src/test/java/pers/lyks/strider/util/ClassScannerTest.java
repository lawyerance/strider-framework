package pers.lyks.strider.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

/**
 * @author lawyerance
 * @version 1.0 2019-08-29
 */
public class ClassScannerTest {
    private static final String packageName = "pers.lyks.strider.util.scan";

    @Test
    public void getTypesAnnotatedWith() {
    }

    @Test
    public void testGetTypesAnnotatedWith() {
        Set<Class<?>> annotatedWith = ClassScanner.getTypesAnnotatedWith(ClassAnnotationTest.class, packageName);
        Assert.assertTrue(annotatedWith.size() > 0);
    }

    @Test
    public void scan() {
    }


}