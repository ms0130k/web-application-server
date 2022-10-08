package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
                OutputStream out = connection.getOutputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                DataOutputStream dos = new DataOutputStream(out);) {
            String line = br.readLine();
            log.debug("Request-Line : {}", line);

            if (line == null) return;
            
            String[] tokens = line.split(" ");
            String uri = tokens[1];
            
            int contentLength = 0;
            while (!"".equals(line)) {
                line = br.readLine();
                log.debug("Header: {}", line);
                if (line.startsWith("Content-Length")) {
                    contentLength = getContentLength(line);
                }
            }
            
            if (uri.startsWith("/user/create")) {
                String queryString = IOUtils.readData(br, contentLength);
                Map<String, String> args = HttpRequestUtils.parseQueryString(queryString);
                User user = new User(args.get("userId"), args.get("password"), args.get("name"), args.get("email"));
                log.debug("User: {}", user);
                uri = "/index.html";
            }

            byte[] body = Files.readAllBytes(new File("./webapp" + uri).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private int getContentLength(String line) {
        String[] headerTokens = line.split(":");
        return Integer.parseInt(headerTokens[1].trim());
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
