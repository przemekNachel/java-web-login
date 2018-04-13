package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import view.getHtml;

import java.io.*;
import java.net.HttpCookie;
import java.sql.SQLException;
import java.time.LocalDateTime;

import dao.*;
import model.*;

public class Login implements HttpHandler {

    private HttpDao httpDao;
    private SessionDao sessionDao;

    private UserDao userDao;
    private HttpExchange httpExchange;
    private String requestMethod;
    private String response;
    private Session session;
    private boolean logged;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println(httpExchange.getRequestURI().toString());
        this.httpDao = new HttpDao(httpExchange);

        this.logged = false;
        this.sessionDao = new SessionDao();
        this.userDao = new UserDao();
        this.httpExchange = httpExchange;
        this.requestMethod = httpExchange.getRequestMethod();

        try {
            makeResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendResponse();
    }

    private void makeResponse() throws Exception{
        if (requestMethod.equals("POST")) {
            if (tryLogin()) {
                response = "<script language=\"JavaScript\" type=\"text/javascript\">location.href=\"/\"</script>";
            }
        } else if (requestMethod.equals("GET")) {
            if (sessionIsValid()) {
                response = "Hello " + session.getUserName() + " Your session ID: " + session.getSessionId();
            } else {
                response = getForm();
            }

        }
    }

    private boolean tryLogin() throws Exception {
        boolean logged = false;
        User user = userDao.getUserByFormData(httpDao.getFormData());
        if (user != null) {
            session = new Session(user.getUsername());
            httpDao.setHttpCookie(session);
            sessionDao.addSession(session);
            logged = true;
        } else {
            response = getForm() + "Bad username or password";
        }
        return logged;
    }

    private boolean sessionIsValid() throws SQLException {
        HttpCookie cookie = httpDao.getHttpCookie();
        if (cookie != null) {
            session = sessionDao.getSessionById(cookie.getValue());
        }
        return session != null && session.getExpireDate().isAfter(LocalDateTime.now());
    }

    private String getForm() {
        return getHtml.html;
    }

    private void sendResponse() throws IOException {
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
