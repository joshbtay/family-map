package Server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class RootHandler implements HttpHandler {
    /**
     * Overrides the HttpHandler's handle method.
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String fileName;
        String tail = exchange.getRequestURI().toString();
        if(tail.equals("/")) {
            fileName = "web/index.html";
        }
        else {
            fileName = "web" + tail;
        }
        System.out.println("Attempting to access file: " + fileName);
        try {
            Path filePath = FileSystems.getDefault().getPath(fileName);
            if(!Files.exists(filePath))
                throw new IOException();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            OutputStream body = exchange.getResponseBody();
            Files.copy(filePath, body);
            body.close();
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
            Path filePath = FileSystems.getDefault().getPath("web/HTML/404.html");
            Files.copy(filePath, exchange.getResponseBody());
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
