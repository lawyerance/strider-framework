package pers.lyks.strider.convert;

/**
 * @author lawyerance
 * @version 1.0 2019-09-10
 */
@FunctionalInterface
public interface Converter<S, D> {
    D convert(S s);
}
