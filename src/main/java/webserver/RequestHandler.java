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
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private boolean logined;
    private int contentLength;

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

            if (line == null)
                return;

            String[] tokens = line.split(" ");
            String uri = getDefaultUri(tokens);
            
            getHeader(br);

            if ("/user/create".equals(uri)) {
                createUser(br, dos, contentLength);
                response302Header(dos, "/index.html");
            } else if ("/user/login".equals(uri)) {
                Map<String, String> body = getRequestBody(br, contentLength);
                User user = DataBase.findUserById(body.get("userId"));
                if (user == null) {
                    responseResource(dos, "/user/login_failed.html");
                    return;
                }
                if (user.getPassword().equals(body.get("password"))) {
                    response302HeaderLoginSuccess(dos, "/index.html");
                    return;
                }
                responseResource(dos, "/user/login_failed.html");
            } else if ("/user/list".equals(uri)) {
                if (logined) {
                    Collection<User> users = DataBase.findAll();
                    StringBuilder sb = new StringBuilder();
                    sb.append("<table>");
                    for (User user : users) {
                        sb.append("<tr>");
                        sb.append("<td>").append(user.getUserId()).append("</td>");
                        sb.append("<td>").append(user.getName()).append("</td>");
                        sb.append("<td>").append(user.getEmail()).append("</td>");
                        sb.append("</tr>");
                    }
                    sb.append("</table>");
                    String body = sb.toString();
                    response200Header(dos, body.length());
                    responseBody(dos, body.getBytes());
                    return;
                }
                response302Header(dos, "/index.html");
                return;
            } else if (uri.endsWith(".css")) {
                byte[] body = Files.readAllBytes(new File("./webapp" + uri).toPath());
                response200HeaderCss(dos, body.length);
                responseBody(dos, body);
                return;
            }
            responseResource(dos, uri);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String getDefaultUri(String[] tokens) {
        String uri = tokens[1];
        if ("/".equals(uri)) {
            uri =  "/index.html";
        }
        return uri;
    }

    public void responseResource(DataOutputStream dos, String uri) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + uri).toPath());
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    public void createUser(BufferedReader br, DataOutputStream dos, int contentLength) throws IOException {
        Map<String, String> body = getRequestBody(br, contentLength);
        User user = new User(body.get("userId"), body.get("password"), body.get("name"), body.get("email"));
        log.debug("User: {}", user);
        DataBase.addUser(user);
    }

    public Map<String, String> getRequestBody(BufferedReader br, int contentLength) throws IOException {
        String queryString = IOUtils.readData(br, contentLength);
        Map<String, String> args = HttpRequestUtils.parseQueryString(queryString);
        return args;
    }

    private void response200HeaderCss(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
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

    private void response302Header(DataOutputStream dos, String uri) {
        try {
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dos.writeBytes("Location: " + uri + " \r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302HeaderLoginSuccess(DataOutputStream dos, String uri) {
        try {
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dos.writeBytes("Location: " + uri + " \r\n");
            dos.writeBytes("Set-Cookie: logined=true \r\n");
            dos.writeBytes("\r\n");
            dos.flush();
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

    private void getHeader(BufferedReader br) throws IOException {
        String line = br.readLine();
        while (!"".equals(line)) {
            line = br.readLine();
            log.debug("Header: {}", line);
            if (line.startsWith("Content-Length")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
            }
            if (line.startsWith("Cookie")) {
                String[] headerTokens = line.split(":");
                Map<String, String> cookies = HttpRequestUtils.parseCookies(headerTokens[1].trim());
                String value = cookies.get("logined");
                logined = Boolean.parseBoolean(value);
            }
        }
    }
}
