package controller;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class Login extends Service {

    private static final String BAD_CREDENTIALS = "Bad username or password";

    private JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/login.twig");
    private JtwigModel model = JtwigModel.newModel();

    @Override
    void handleGetFromValidatedUser() {
        redirectTo("/");
    }

    @Override
    void handleGetFromUnvalidatedUser() {
        model.with("message" , "");
        setResponse(template.render(model));
    }

    @Override
    void handlePostFromValidatedUser() {
        redirectTo("/");
    }

    @Override
    void handlePostFromUnvalidatedUser() {
        model.with("message" , BAD_CREDENTIALS);
        setResponse(template.render(model));
    }
}