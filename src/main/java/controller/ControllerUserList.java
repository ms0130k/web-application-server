package controller;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

public class ControllerUserList extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(ControllerUserList.class);

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        if (!request.isLogined()) {
            response.sendRedirect("/index.html");
            return;
        }
        Collection<User> users = DataBase.findAll();
        response.forwardBody(writeUserList(users));
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

    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        log.debug("method: {}", request.getMethod());
    }
}
