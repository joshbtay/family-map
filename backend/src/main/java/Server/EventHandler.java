package Server;

import Request.EventRequest;
import Result.EventResult;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.EventService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class EventHandler implements HttpHandler {
    @Override
    /**
     * Overrides the HttpHandler's handle method.
     * @param exchange
     * @throws IOException
     */
    public void handle(HttpExchange exchange) throws IOException {

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                Headers headers = exchange.getRequestHeaders();
                if (headers.containsKey("Authorization")) {
                    String authToken = headers.getFirst("Authorization");
                    String tail = exchange.getRequestURI().toString();
                    EventService eventService = new EventService();
                    Gson gson = new Gson();
                    EventRequest request;
                    if(tail.contains("/event/")){
                        request = new EventRequest(tail.substring(7), authToken);
                    }
                    else{
                        request = new EventRequest(authToken);
                    }
                    EventResult eventResult = eventService.event(request);
                    String response = gson.toJson(eventResult);
                    if(eventResult.getSuccess()){
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }
                    else{
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    }
                    OutputStream body = exchange.getResponseBody();
                    write(body, response);
                    body.close();
                }
            }
        } catch (IOException e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

    private void write(OutputStream os, String input) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(os);
        outputStreamWriter.write(input);
        outputStreamWriter.flush();
    }
}
