package pers.lyks.strider.annotation;

import pers.lyks.strider.codec.Decoder;
import pers.lyks.strider.codec.JacksonDecoder;

import java.lang.annotation.*;

/**
 * @author lawyerance
 * @version 1.0 2019-08-06
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface ResponseBody {
    Class<? extends Decoder> value() default JacksonDecoder.class;
}
