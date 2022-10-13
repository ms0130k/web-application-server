package webserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

public class HttpResponseTest {
    private String testDirectory = "./src/test/resources/";
    private HttpResponse response;

    @Before
    public void setHttpResponse() throws FileNotFoundException {
        response = new HttpResponse(createOutputStream("Http_Forward.txt"));
    }
    
    @Test
    public void responseForward() throws FileNotFoundException {
        response.forward("/index.html");;
    }
    
    @Test
    public void responseRedirect() {
        response.sendRedirect("/index.html");
    }
    
    @Test
    public void responseCookies() {
        response.addHeader("Set-Cookie", "logined=true");
        response.sendRedirect("/index.html");
    }

    private OutputStream createOutputStream(String filename) throws FileNotFoundException {
        return new FileOutputStream(new File(testDirectory + filename));
    }
}
