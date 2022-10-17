package webserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.junit.Test;

import http.HttpResponse;

public class HttpResponseTest {
    private String testDirectory = "./src/test/resources/";
    private HttpResponse response;

    @Test
    public void responseForward() throws FileNotFoundException {
        getResponse("Http_Forward.txt");
        response.forward("/index.html");;
    }
    
    @Test
    public void responseRedirect() throws FileNotFoundException {
        getResponse("Http_Redirect.txt");
        response.sendRedirect("/index.html");
    }
    
    @Test
    public void responseCookies() throws FileNotFoundException {
        getResponse("Http_Cookie.txt");
        response.addHeader("Set-Cookie", "logined=true");
        response.sendRedirect("/index.html");
    }
    
    private void getResponse(String fileName) throws FileNotFoundException {
        response = new HttpResponse(createOutputStream(fileName));
    }

    private OutputStream createOutputStream(String filename) throws FileNotFoundException {
        return new FileOutputStream(new File(testDirectory + filename));
    }
}
