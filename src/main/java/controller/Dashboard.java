package controller;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class Dashboard extends Service {

    private JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/index.twig");
    private JtwigModel model = JtwigModel.newModel();

    @Override
    void handleGetFromValidatedUser() {
        model.with("username" , getSession().getUserName());
        model.with("session_id" , getSession().getSessionId());
        setResponse(template.render(model));
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
        redirectTo("/login");
    }
}