package controller;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class Logout extends Service {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        super.handle(httpExchange);
    }

    @Override
    void handleGetFromValidatedUser() {
        redirectTo("/login");
    }

    @Override
    void handleGetFromUnvalidatedUser() {
        redirectTo("/login");
    }

    @Override
    void handlePostFromValidatedUser() {
        redirectTo("/login");
    }

    @Override
    void handlePostFromUnvalidatedUser() {
        redirectTo("/login");
    }
}