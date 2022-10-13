package webserver;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class HttpRequestTest {
    private String testDirectory = "./src/test/resources/";

    @Test
    public void request_GET() throws Exception {
//        InputStream in = new FileInputStream(new File(testDirectory + "Http_GET.txt"));
        String text = "GET /user/create?userId=javajigi&password=password&name=JaeSung HTTP/1.1\r\n" + 
                "Host: localhost:8080\r\n" + 
                "Connection: keep-alive\r\n" + 
                "Accept: */*\r\n" + 
                "";
        InputStream is = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        HttpRequest request = new HttpRequest(is);

        assertEquals("GET", request.getMethod());
        assertEquals("/user/create", request.getPath());
        assertEquals("keep-alive", request.getHeader("Connection"));
        assertEquals("javajigi", request.getParameter("userId"));
    }

    @Test
    public void request_POST() throws Exception {
//        InputStream in = new FileInputStream(new File(testDirectory + "Http_POST.txt"));
        String text = "POST /user/create HTTP/1.1\r\n" + 
                "Host: localhost:8080\r\n" + 
                "Connection: keep-alive\r\n" + 
                "Content-Length: 46\r\n" + 
                "Content-Type: application/x-www-form-urlencoded\r\n" + 
                "Accept: */*\r\n" + 
                "\r\n" + 
                "userId=javajigi&password=password&name=JaeSung";
        InputStream is = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        HttpRequest request = new HttpRequest(is);

        assertEquals("POST", request.getMethod());
        assertEquals("/user/create", request.getPath());
        assertEquals("keep-alive", request.getHeader("Connection"));
        assertEquals("javajigi", request.getParameter("userId"));
    }
}
