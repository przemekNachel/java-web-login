package main;

import com.sun.net.httpserver.HttpServer;
import controller.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class  App {

    public static void main(String[] args) {

        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/", new Dashboard());
            server.createContext("/login", new Login());
            server.createContext("/logout", new Logout());
            server.createContext("/static", new Static());
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
