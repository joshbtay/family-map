package Server;

import Request.FillRequest;
import Request.RegisterRequest;
import Result.FillResult;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.FillService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.Scanner;

public class FillHandler implements HttpHandler {
    /**
     * Overrides the HttpHandler's handle method.
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Received fill request");
        try {
            Gson gson = new Gson();
            if(exchange.getRequestMethod().toLowerCase().equals("post")){
                String tail = exchange.getRequestURI().toString();
                String[] split = tail.split("/");
                String username = split[2];
                FillRequest fillRequest;
                if(split.length > 3){
                    int generations = Integer.parseInt(split[3]);
                    fillRequest = new FillRequest(username, generations);
                }
                else {
                    fillRequest = new FillRequest(username);
                }
                FillService fillService = new FillService();
                FillResult fillResult = fillService.fill(fillRequest);
                String response = gson.toJson(fillResult);
                if(fillResult.getSuccess()){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                else{
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                OutputStream outputStream = exchange.getResponseBody();
                write(outputStream, response);
                outputStream.close();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    private void write(OutputStream os, String input) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(os);
        outputStreamWriter.write(input);
        outputStreamWriter.flush();
    }
}
