package controller;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.sql.SQLException;

public class Logout extends Service {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        super.handle(httpExchange);
    }

    @Override
    void handleGetFromValidatedUser() {
        try {
            deleteSession();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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