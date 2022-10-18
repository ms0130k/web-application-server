package http;

import static org.junit.Assert.*;

import org.junit.Test;

public class RequestParamTest {

    @Test
    public void add() {
        RequestParam param = new RequestParam();

        param.addQueryString("id=1");
        param.addQueryString("userId=javajigi&password=password");
        assertEquals("1", param.get("id"));
        assertEquals("javajigi", param.get("userId"));
        assertEquals("password", param.get("password"));
    }

}
