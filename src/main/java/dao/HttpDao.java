package dao;

import com.sun.net.httpserver.HttpExchange;
import model.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpDao {

    private HttpExchange httpExchange;
    private HttpCookie httpCookie;

    public HttpDao(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    public HttpCookie getHttpCookie() {
        HttpCookie cookie = null;
        String cookiesHeader = httpExchange.getRequestHeaders().getFirst("Cookie");
        List<HttpCookie> cookieList = getAllCookies(cookiesHeader);
        if (cookieList != null) {
            for (HttpCookie cookieFromList : cookieList) {
                if (cookieFromList.getName().equals("sessionId")) {
                    cookie = cookieFromList;
                }
            }
        }
        return cookie;
    }

    public static List<HttpCookie> getAllCookies(String cookiesHeader) {
        List<HttpCookie> cookiesList = new ArrayList<>();
        String[] singleCookie = cookiesHeader.split(";");
        for (String c : singleCookie) {
            List<HttpCookie> l = HttpCookie.parse(c);
            cookiesList.add(l.get(0));
        }
        return cookiesList;
    }

    public void setHttpCookie(Session session) {
        httpCookie = new HttpCookie("sessionId", session.getSessionId());
        httpExchange.getResponseHeaders().add("Set-Cookie", httpCookie.toString());
    }

    public void setCookieToNull() {
        httpCookie = new HttpCookie("sessionId", "null");
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
