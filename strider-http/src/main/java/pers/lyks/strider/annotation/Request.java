package pers.lyks.strider.annotation;

import java.lang.annotation.*;

/**
 * @author lawyerance
 * @version 1.0 2019-09-06
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Request {

    String uri() default "";

    Method method() default Method.GET;

    String connectTimeout() default "30s";

    String readTimeout() default "15s";

    boolean followRedirect() default false;

    Header[] headers() default {};
}
