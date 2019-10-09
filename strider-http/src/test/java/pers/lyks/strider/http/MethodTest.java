package pers.lyks.strider.http;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pers.lyks.strider.annotation.Method;

/**
 * @author lawyerance
 * @version 1.0 2019-08-30
 */
public class MethodTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void nullMethodName() {
        String errorMessage = "http method may be not null or out of method range: [ " + Method.valueChain() + " ], with method name: null.";
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMessage);
        Method.nameOf(null);
    }


    @Test
    public void outMethodName() {
        String outMethod = "DO";
        String errorMessage = "http method may be not null or out of method range: [ " + Method.valueChain() + " ], with method name: " + outMethod + ".";
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMessage);
        Method.nameOf(outMethod);
    }


    @Test
    public void validMethodName() {
        String validMethod = "Get";
        Method method = Method.nameOf(validMethod);
        Assert.assertEquals(method, Method.GET);
    }
}
