package Server;
import Request.PersonRequest;
import Result.PersonResult;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.PersonService;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class PersonHandler implements HttpHandler {
    /**
     * Overrides the HttpHandler's handle method.
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Received person request");
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                Headers headers = exchange.getRequestHeaders();
                if (headers.containsKey("Authorization")) {
                    String authToken = headers.getFirst("Authorization");
                    String tail = exchange.getRequestURI().toString();
                    PersonService personService = new PersonService();
                    Gson gson = new Gson();
                    PersonRequest request;
                    if(tail.contains("/person/")){
                        request = new PersonRequest(tail.substring(8), authToken);
                    }
                    else{
                        request = new PersonRequest(authToken);
                    }
                    PersonResult result = personService.person(request);
                    String response = gson.toJson(result);
                    if(result.getSuccess()){
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }
                    else {
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
