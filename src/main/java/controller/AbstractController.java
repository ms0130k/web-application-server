package controller;

import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;

public abstract class AbstractController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        HttpMethod method = request.getMethod();

        if (method.isGet()) {
            doGet(request, response);
        } else {
            doPost(request, response);
        }
    }

    protected void doGet(HttpRequest request, HttpResponse response) {

    }

    protected void doPost(HttpRequest request, HttpResponse response) {

    }
}
