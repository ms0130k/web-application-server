package webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpSessions;

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
            
            log.debug("쿠키값: {}, 비었는지: {}", request.getCookie().get(HttpSessions.SESSION_ID_NAME), StringUtils.isEmpty(request.getCookie().get(HttpSessions.SESSION_ID_NAME)));
            if (StringUtils.isEmpty(request.getCookie().get(HttpSessions.SESSION_ID_NAME))) {
                UUID uuid = UUID.randomUUID();
                log.debug("발급받은 uuid: {}", uuid);
                response.addHeader("Set-Cookie", HttpSessions.SESSION_ID_NAME + "=" + uuid);
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
