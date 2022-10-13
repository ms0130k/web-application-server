
package webserver;

import java.io.OutputStream;

public class HttpResponse {

    private OutputStream os;

    public HttpResponse(OutputStream os) {
        this.os = os;
    }
    
    public void forward(String path) {
        
    }
    
    public void sendRedirect(String path) {
        
    }
    
    public void addHeader(String key, String value) {
        
    }
    
}
