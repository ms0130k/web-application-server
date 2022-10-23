package webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import http.HttpRequest;
import http.HttpResponse;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream();) {
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);
            
            String sessionId = request.getCookie("JSESSIONID");
            if (StringUtils.isEmpty(sessionId)) {
                response.addHeader("Set-Cookie", "JSESSIONID=" + UUID.randomUUID());
            }
            
            String path = request.getPath();
            Controller controller = RequestMapping.getController(path);
            
            if (controller == null) {
                response.forward(getDefaultUrl(path));
            } else {
                controller.service(request, response);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String getDefaultUrl(String path) {
        return "/".equals(path) ? "/index.html" : path;
    }
}
