package dao;

import com.sun.net.httpserver.HttpExchange;
import model.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpDao {

    private HttpExchange httpExchange;
    private HttpCookie httpCookie;

    public HttpDao(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        this.httpCookie = (httpExchange.getRequestHeaders().getFirst("Cookie") != null) ?
                HttpCookie.parse(httpExchange.getRequestHeaders().getFirst("Cookie")).get(0) :
                null;
    }

    public HttpCookie getHttpCookie() {
        return httpCookie;
    }

    public void setHttpCookie(Session session) {
        httpCookie = new HttpCookie("sessionId", session.getSessionId());
        httpExchange.getResponseHeaders().add("Set-Cookie", httpCookie.toString());
    }

    public Map<String, String> getFormData() throws IOException {
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        String formDataString = new BufferedReader(isr).readLine();
        Map<String, String> map = null;
        if (formDataString != null) {
            map = new HashMap<>();
            for (String pair : formDataString.split("&")) {
                String[] keyValue = pair.split("=");
                map.put(keyValue[0], new URLDecoder().decode(keyValue.length > 1 ? keyValue[1] : "", "UTF-8"));
            }
        }
        return map;
    }
}
