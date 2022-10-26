package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

public class UserCreationController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(UserCreationController.class);

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        User user = new User(request.getParameter("userId"), request.getParameter("password"),
                request.getParameter("name"), request.getParameter("email"));
        log.debug("User: {}", user);
        DataBase.addUser(user);
        response.sendRedirect("/index.html");
    }
}