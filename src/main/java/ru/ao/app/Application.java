package ru.ao.app;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import ru.ao.app.endpoint.config.AppConfig;

import java.io.IOException;
import java.net.URI;

public class Application {

    public static final URI BASE_URI = URI.create("http://localhost:8080/acc-app/");

    public static void main(String[] args) throws IOException, InterruptedException {
        final HttpServer server = GrizzlyHttpServerFactory
                .createHttpServer(BASE_URI, new AppConfig(), false);
        System.out.println(String.format("Account Manager app started. Try on "
                + "%s\nHit Ctrl + C to stop it...", BASE_URI));
        Runtime.getRuntime()
                .addShutdownHook(new Thread(server::shutdownNow));
        server.start();
        Thread.currentThread().join();
    }

}