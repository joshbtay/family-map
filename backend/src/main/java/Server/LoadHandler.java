package Server;

import Request.LoadRequest;
import Result.LoadResult;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.LoadService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.Scanner;

public class LoadHandler implements HttpHandler {
    /**
     * Overrides the HttpHandler's handle method.
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Received load request");
        try {
            Gson gson = new Gson();
            if(exchange.getRequestMethod().toLowerCase().equals("post")){
                InputStream inputStream = exchange.getRequestBody();
                Scanner s = new Scanner(inputStream).useDelimiter("\\A");
                String requestBody = s.hasNext() ? s.next() : "";
                LoadRequest loadRequest = gson.fromJson(requestBody, LoadRequest.class);
                System.out.println("Length " + loadRequest.getUsers().length);
                LoadService loadService = new LoadService();
                LoadResult loadResult = loadService.load(loadRequest);
                String response = gson.toJson(loadResult);
                if(loadResult.getSuccess()){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                OutputStream outputStream = exchange.getResponseBody();
                write(outputStream, response);
                outputStream.close();
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