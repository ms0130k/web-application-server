package webserver;

import static org.junit.Assert.*;

import org.junit.Test;

public class RequestLineTest {

    @Test
    public void get() {
        String line = "GET /index.html HTTP/1.1";
        RequestLine requestLine = new RequestLine(line);
        assertEquals("GET", requestLine.getMethod());
        assertEquals("/index.html", requestLine.getPath());
        
        line = "GET /user/create?userId=javajigi&password=password&name=JaeSung HTTP/1.1";
        requestLine = new RequestLine(line);
        assertEquals(3, requestLine.getParams().size());
        assertEquals("javajigi", requestLine.getParams().get("userId"));
    }
    
    @Test
    public void post() {
        String line = "POST /index.html HTTP/1.1";
        RequestLine requestLine = new RequestLine(line);
        assertEquals("POST", requestLine.getMethod());
        assertEquals("/index.html", requestLine.getPath());
    }
}
