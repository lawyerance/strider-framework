package pers.lyks.strider.pasring;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

/**
 * @author lawyerance
 * @version 1.0 2019-09-11
 */
public class DefaultPropertyParserTest {

    private static final String CONTENT = "/{id}/{name:jack}/jsjsj/{age-22}/--123:/${grade:123}/${line-11}";
    private final Properties BASE_PROPERTIES = new Properties();
    private static final PropertyParser parser = new DefaultPropertyParser();

    @Before
    public void before() {
        BASE_PROPERTIES.put("id", "123");
    }

    @Test
    public void parseOnlyOne() {
        Properties p1 = new Properties();
        p1.putAll(BASE_PROPERTIES);
        String s = parser.parse(CONTENT, p1);
        Assert.assertEquals(s, "/123/{name:jack}/jsjsj/{age-22}/--123:/${grade:123}/${line-11}");
    }

    @Test
    public void parseWithDefaultValueSeparatorByColon() {
        Properties p1 = new Properties();
        p1.putAll(BASE_PROPERTIES);
        p1.put("pers.lyks.strider.pasring.PropertyParser.enable-default-value", "true");
        p1.put("grade", "100");
        p1.put("line", "100");
        String s = parser.parse(CONTENT, p1);
        Assert.assertEquals(s, "/123/jack/jsjsj/{age-22}/--123:/$100/${line-11}");
    }

    @Test
    public void parseWithDefaultValueSeparatorByLine() {
        Properties p1 = new Properties();
        p1.putAll(BASE_PROPERTIES);
        p1.put("pers.lyks.strider.pasring.PropertyParser.enable-default-value", "true");
        p1.put("pers.lyks.strider.pasring.PropertyParser.default-value-separator", "-");
        p1.put("grade", "100");
        p1.put("line", "100");
        String s = parser.parse(CONTENT, p1);
        Assert.assertEquals(s, "/123/{name:jack}/jsjsj/22/--123:/${grade:123}/$100");
    }
}
