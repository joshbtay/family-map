package Server;

import Request.LoginRequest;
import Result.LoginResult;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.LoginService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.Scanner;

public class LoginHandler implements HttpHandler {
    /**
     * Overrides the HttpHandler's handle method.
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Received login request");
        try {
            Gson gson = new Gson();
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                InputStream inputStream = exchange.getRequestBody();
                Scanner s = new Scanner(inputStream).useDelimiter("\\A");
                String requestBody = s.hasNext() ? s.next() : "";
                LoginRequest loginRequest = gson.fromJson(requestBody, LoginRequest.class);
                LoginService loginService = new LoginService();
                LoginResult loginResult = loginService.login(loginRequest);
                String response;
                if(!loginResult.getSuccess()){
                    response = gson.toJson(loginResult);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                else {
                    response = gson.toJson(loginResult);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }


                OutputStream body = exchange.getResponseBody();
                write(body, response);
                body.close();
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
