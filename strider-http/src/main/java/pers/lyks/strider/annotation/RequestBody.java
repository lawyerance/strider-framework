package pers.lyks.strider.annotation;

import java.lang.annotation.*;

/**
 * @author lawyerance
 * @version 1.0 2019-08-10
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface RequestBody {

}
