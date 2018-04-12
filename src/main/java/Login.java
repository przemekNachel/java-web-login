import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class Login implements HttpHandler {

    private HttpExchange httpExchange;
    private String requestMethod;
    private Map<String, String> formData;
    private String response;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        this.httpExchange = httpExchange;
        this.requestMethod = httpExchange.getRequestMethod();

        makeResponse();
        sendResponse();
    }

    private void makeResponse() throws IOException{
        if (requestMethod.equals("POST")) {
            getFormData();
        } else if (requestMethod.equals("GET")) {
            response = new Session("Przemek").getSessionId();
        }
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
