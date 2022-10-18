package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

public class ControllerLogin extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(ControllerLogin.class);

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        User user = DataBase.findUserById(request.getParameter("userId"));
        if (user != null && user.getPassword().equals(request.getParameter("password"))) {
            response.addHeader("Set-Cookie", "logined=true; Path=/");
            response.sendRedirect("/index.html");
        } else {
            response.forward("/user/login_failed.html");
        }
    }

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        log.debug("method: {}", request.getMethod());
    }
}
