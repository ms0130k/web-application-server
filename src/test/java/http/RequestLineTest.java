package http;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RequestLineTest {

    @Test
    public void get() {
        String line = "GET /index.html HTTP/1.1";
        RequestLine requestLine = new RequestLine(line);
        assertEquals(HttpMethod.GET, requestLine.getMethod());
        assertEquals("/index.html", requestLine.getPath());
        
        line = "GET /user/create?userId=javajigi&password=password&name=JaeSung HTTP/1.1";
        requestLine = new RequestLine(line);
        assertEquals("userId=javajigi&password=password&name=JaeSung", requestLine.getQueryString());
    }
    
    @Test
    public void post() {
        String line = "POST /index.html HTTP/1.1";
        RequestLine requestLine = new RequestLine(line);
        assertEquals(HttpMethod.POST, requestLine.getMethod());
        assertEquals("/index.html", requestLine.getPath());
    }
}
