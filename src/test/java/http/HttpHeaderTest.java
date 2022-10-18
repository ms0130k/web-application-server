package http;

import static org.junit.Assert.*;

import org.junit.Test;

public class HttpHeaderTest {

    @Test
    public void add() {
        HttpHeader headers = new HttpHeader();
        headers.add("Connection: keep-alive");
        assertEquals("keep-alive", headers.get("Connection"));
    }
}
