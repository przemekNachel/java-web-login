import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Login implements HttpHandler {

    private SessionDao sessionDao;
    private HttpExchange httpExchange;
    private String requestMethod;
    private Map<String, String> formData;
    private String response;
    private Session session;
    private HttpCookie cookie;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        this.sessionDao = new SessionDao();
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
            if (sessionUnexpired()) {
                response = "Hello " + session.getUserName() + "Your session ID: " + session.getSessionId();
            }
        } else if (requestMethod.equals("GET")) {
            if (sessionUnexpired()) {
                response = "Hello " + session.getUserName() + "Your session ID: " + session.getSessionId();
            } else {
                response = getForm();
            }

        }
    }


    private void tryLogin() throws SQLException {
        String correctUsername = "tadek";
        String correctPassword = "przemek";

        String username = formData.get("username");

        if (username.equals(correctUsername) && formData.get("password").equals(correctPassword)) {
            session = new Session(username);
            setCookie();
            sessionDao.addSession(session);
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
        if (session != null && session.getExpireDate().isAfter(LocalDateTime.now())) {
            return true;
        }
        return false;
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
