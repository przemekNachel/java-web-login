package controller;

import java.sql.SQLException;

public class Logout extends Service {

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