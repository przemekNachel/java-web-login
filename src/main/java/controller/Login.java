package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import view.getHtml;

import java.io.*;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import dao.*;
import model.*;

public class Login implements HttpHandler {

    private SessionDao sessionDao;
    private UserDao userDao;
    private HttpExchange httpExchange;
    private String requestMethod;
    private Map<String, String> formData;
    private String response;
    private Session session;
    private HttpCookie cookie;
    private boolean logged;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        System.out.println(httpExchange.getRequestURI().toString());
        this.logged = false;
        this.sessionDao = new SessionDao();
        this.userDao = new UserDao();
        this.cookie = null;
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
            getFormData();
            tryLogin();
            if (logged) {
                response = "<script language=\"JavaScript\" type=\"text/javascript\">location.href=\"/\"</script>";
            }
        } else if (requestMethod.equals("GET")) {
            if (sessionUnexpired()) {
                response = "Hello " + session.getUserName() + " Your session ID: " + session.getSessionId();
            } else {
                response = getForm();
            }

        }
    }


    private void tryLogin() throws SQLException {
        String username = formData.get("username");
        if (userDao.getUserByUsername(username, formData.get("password")) != null) {
            session = new Session(username);
            setCookie();
            sessionDao.addSession(session);
            logged = true;
        } else {
            response = getForm() + "Bad username or password";
        }
    }

    private void setCookie() {
        cookie = new HttpCookie("sessionId", session.getSessionId());
        httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
    }

    private void getCookie() {
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        if (cookieStr != null) {
            cookie = HttpCookie.parse(cookieStr).get(0);
        }
    }

    private boolean sessionUnexpired() throws SQLException {
        getCookie();
        if (cookie != null) {
            session = sessionDao.getSessionById(cookie.getValue());
        }
        return session != null && session.getExpireDate().isAfter(LocalDateTime.now());
    }

    private String getForm() {
        return getHtml.html;
    }

    private void sendResponse() throws IOException{
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void getFormData() throws IOException {
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        String formDataString = new BufferedReader(isr).readLine();
        if (formDataString != null) {
            Map<String, String> map = new HashMap<>();
            for (String pair : formDataString.split("&")) {
                String[] keyValue = pair.split("=");
                map.put(keyValue[0], new URLDecoder().decode(keyValue.length > 1 ? keyValue[1] : "", "UTF-8"));
            }
            formData = map;
        }
    }
}
