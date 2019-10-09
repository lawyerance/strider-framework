package pers.lyks.strider.execution;

import org.junit.*;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.verify.VerificationTimes;
import pers.lyks.strider.Configuration;
import pers.lyks.strider.client.StriderJsonResponseGet;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lawyerance
 * @version 1.0 2019-09-03
 */
public class StriderJsonResponseGetTest {
    private static final int PORT = 10082;

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, PORT);

    private MockServerClient mockServerClient;
    private String resultValue = "{\"key\":\"shuangshuang\",\"value\":\"I love you forever.\"}";
    private String url = "/search?keyword=import";

    private HttpRequest request;

    private StriderJsonResponseGet striderGet;

    @Before
    public void before() {
        request = HttpRequest.request("/get/search").withQueryStringParameter("keyword", "import");
        mockServerClient.when(request)
                .respond(HttpResponse.response(resultValue));


        Configuration configuration = new Configuration();
        configuration.scan("pers.lyks.strider.client");
        striderGet = configuration.getRequestRegistry().getRequest(StriderJsonResponseGet.class, null);
    }

    @Test
    public void get() throws Exception {
        Map<String, Object> s = striderGet.get();
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("key", "shuangshuang");
        returnMap.put("value", "I love you forever.");
        Assert.assertEquals(returnMap, s);
    }


    @After
    public void after() {
        mockServerClient.verify(request, VerificationTimes.once());
    }
}
