package webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private HttpRequest request;
    private HttpResponse response;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream();) {
            request = new HttpRequest(in);
            response = new HttpResponse(out);

            String path = getDefaultUri(request.getPath());

            if ("/user/create".equals(path)) {
                createUser();
                response.sendRedirect("/index.html");
            } else if ("/user/login".equals(path)) {
                User user = DataBase.findUserById(request.getParameter("userId"));
                if (user != null && user.getPassword().equals(request.getParameter("password"))) {
                    response.sendRedirect("/index.html");
                } else {
                    response.forward("/user/login_failed.html");
                }
            } else if ("/user/list".equals(path)) {
                if (!request.isLogined()) {
                    response.sendRedirect("/index.html");
                } else {
                    Collection<User> users = DataBase.findAll();
                    response.forwardBody(writeUserList(users));
                }
            } else {
                response.forward(path);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String writeUserList(Collection<User> users) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table>");
        for (User user : users) {
            sb.append("<tr>");
            sb.append("<td>").append(user.getUserId()).append("</td>");
            sb.append("<td>").append(user.getName()).append("</td>");
            sb.append("<td>").append(user.getEmail()).append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }

    private String getDefaultUri(String path) {
        if ("/".equals(path)) {
            path = "/index.html";
        }
        return path;
    }

    public void createUser() throws IOException {
        User user = new User(request.getParameter("userId"), request.getParameter("password"),
                request.getParameter("name"), request.getParameter("email"));
        log.debug("User: {}", user);
        DataBase.addUser(user);
    }

}
