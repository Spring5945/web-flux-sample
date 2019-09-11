package org.springframework.web.reactive.samples;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.ipc.netty.http.server.HttpServer;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;

public class Server {

    public static final String HOST = "localhost";

    public static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.startReactorServer();

        System.out.println("Press ENTER to exit.");
        System.in.read();
    }

    public void startReactorServer() throws InterruptedException {
        RouterFunction<ServerResponse> route = routingFunction();
        HttpHandler httpHandler = toHttpHandler(route);

        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
        HttpServer server = HttpServer.create(HOST, PORT);
        server.newHandler(adapter).block();
    }

    public RouterFunction<ServerResponse> routingFunction() {
        PersonRepository repository = new NickPersonRepository();
        PersonHandler handler = new PersonHandler(repository);

        return nest(path("/person"),
                nest(accept(APPLICATION_JSON),
                        route(GET("/{id}"), handler::getPerson)
                                .andRoute(method(HttpMethod.GET), handler::listPeople)
                ).andRoute(POST("/").and(contentType(APPLICATION_JSON)), handler::createPerson));
    }
}
