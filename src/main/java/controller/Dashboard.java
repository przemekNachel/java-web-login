package controller;

import view.getHtml;

public class Dashboard extends Service {

    @Override
    void handleGetFromValidatedUser() {
        setResponse(getHtml.mainPage(getSession().getUserName(), getSession().getSessionId()));
    }

    @Override
    void handleGetFromUnvalidatedUser() {
        redirectTo("/login");
    }

    @Override
    void handlePostFromValidatedUser() {
        redirectTo("/");
    }

    @Override
    void handlePostFromUnvalidatedUser() {
        setResponse(getHtml.badCredentials);
    }
}