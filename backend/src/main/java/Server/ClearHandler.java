package Server;

import Request.ClearRequest;
import Result.ClearResult;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.ClearService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class ClearHandler implements HttpHandler {
    /**
     * Overrides the HttpHandler's handle method.
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Received clear request");
        try {
            Gson gson = new Gson();
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                ClearRequest clearRequest = new ClearRequest();
                ClearService clearService = new ClearService();
                ClearResult clearResult = clearService.clear(clearRequest);
                String response = gson.toJson(clearResult);
                if (clearResult.getSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                OutputStream outputStream = exchange.getResponseBody();
                write(outputStream, response);
                outputStream.close();
            }
        }

        catch (IOException e) {
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
