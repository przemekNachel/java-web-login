package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpCookie;
import java.sql.SQLException;
import java.time.LocalDateTime;

import dao.*;
import model.*;

public abstract class Service implements HttpHandler {

    private HttpDao httpDao;
    private SessionDao sessionDao;
    private UserDao userDao;
    private HttpExchange httpExchange;
    private String requestMethod;
    private Session session;
    private String response;
    private User user;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException{

        this.httpExchange = httpExchange;
        this.requestMethod = httpExchange.getRequestMethod();
        this.httpDao = new HttpDao(httpExchange);
        this.sessionDao = new SessionDao();
        this.userDao = new UserDao();

        try {
            sendResponse(getResponse());
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
        }
    }

    abstract void handleGetFromValidatedUser();

    abstract void handleGetFromUnvalidatedUser();

    abstract void handlePostFromValidatedUser();

    abstract void handlePostFromUnvalidatedUser();

    Session getSession() {
        return session;
    }

    void setResponse(String response) {
        this.response = response;
    }

    private String getResponse() throws SQLException, IOException{

        response = "";
        if (requestMethod.equals("GET")) {
            if (sessionIsValid()) {
                sessionDao.updateLastAccessDate(session);
                handleGetFromValidatedUser();
            } else {
                handleGetFromUnvalidatedUser();
            }
        } else if (requestMethod.equals("POST")) {
            if (sessionIsValid() || isLogged()) {
                sessionDao.updateLastAccessDate(session);
                handlePostFromValidatedUser();
            } else {
                handlePostFromUnvalidatedUser();
            }
        }

        return response;
    }

    void redirectTo(String address) {
        response = "<script language=\"JavaScript\" type=\"text/javascript\">location.href=\"" + address + "\"</script>";
    }

    void deleteSession() throws SQLException{
        if (session != null) {
            sessionDao.deleteSession(session);
        }
    }

    private boolean isLogged() throws SQLException, IOException{
        boolean isLogged = false;
        user = userDao.getUserByFormData(httpDao.getFormData());
        if (user != null) {
            session = new Session(user.getUsername());
            httpDao.setHttpCookie(session);
            sessionDao.addSession(session);
            isLogged = true;
        }
        return isLogged;
    }

    private boolean sessionIsValid() throws SQLException {
        HttpCookie cookie = httpDao.getHttpCookie();
        session = null;
        if (cookie != null) {
            session = sessionDao.getSessionById(cookie.getValue());
        }
        return session != null && session.getExpireDate().isAfter(LocalDateTime.now());
    }

    private void sendResponse(String response) throws IOException{
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
