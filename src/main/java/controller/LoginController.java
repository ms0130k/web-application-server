package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

public class LoginController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        if (login(request)) {
            response.sendRedirect("/index.html");
            return;
        }
        response.forward("/user/login_failed.html");
    }
    
    private boolean login(HttpRequest request) {
        User user = DataBase.findUserById(request.getParameter("userId"));
        if (user == null || !user.getPassword().equals(request.getParameter("password"))) {
            return false;
        }
        request.getSession().setAttribute("user", user);
        return true;
    }
}
