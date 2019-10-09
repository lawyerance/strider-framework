package pers.lyks.strider.annotation;

import java.lang.annotation.*;

/**
 * @author lawyerance
 * @version 1.0 2019-08-07
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
public @interface Header {
    String name();

    String[] value();
}
