package pers.lyks.strider.annotation;

import java.lang.annotation.*;


/**
 * @author lawyerance
 * @version 1.0 2019-09-09
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface HeaderMap {
}
