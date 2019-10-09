package pers.lyks.strider.annotation;

import java.lang.annotation.*;


/**
 * @author lawyerance
 * @version 1.0 2019-08-06
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Client {

    String host() default "http://127.0.0.1";

    String context() default "";

}
