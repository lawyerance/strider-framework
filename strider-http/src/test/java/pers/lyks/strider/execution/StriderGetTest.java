package pers.lyks.strider.execution;

import org.junit.*;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.verify.VerificationTimes;
import pers.lyks.strider.Configuration;
import pers.lyks.strider.client.StriderGet;

/**
 * @author lawyerance
 * @version 1.0 2019-09-03
 */
public class StriderGetTest {
    private static final int PORT = 10081;

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, PORT);

    private MockServerClient mockServerClient;
    private String resultValue = "111";
    private String url = "/search?keyword=import";

    private HttpRequest request;

    private StriderGet striderGet;

    @Before
    public void before() {
        request = HttpRequest.request("/get/search").withQueryStringParameter("keyword", "import");
        mockServerClient.when(request)
                .respond(HttpResponse.response(resultValue));


        Configuration configuration = new Configuration();
        configuration.scan("pers.lyks.strider.client");
        striderGet = configuration.getRequestRegistry().getRequest(StriderGet.class, null);
    }

    @Test
    public void get() throws Exception {
        String s = striderGet.get();
        Assert.assertEquals(resultValue, s);
    }

    @Test
    public void getByteArray() throws Exception {
        byte[] bytes = striderGet.getByteArray();
        Assert.assertEquals(resultValue, new String(bytes));
    }

    @Test
    public void discarding() throws Exception {
        striderGet.discarding();
    }

    @After
    public void after() {
        mockServerClient.verify(request, VerificationTimes.atMost(3));
    }
}
