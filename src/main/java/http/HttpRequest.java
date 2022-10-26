package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.IOUtils;

public class HttpRequest {
    private final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    
    private RequestLine requestLine;
    private HttpHeader header;
    private RequestParam param = new RequestParam();

    public HttpRequest(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            requestLine = new RequestLine(createRequestLine(br));
            param.addQueryString(requestLine.getQueryString());
            header = processHeaders(br);
            param.addQueryString(IOUtils.readData(br, header.getContentLength()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (line == null) {
            throw new IllegalStateException();
        }
        return line;
    }
    
    private HttpHeader processHeaders(BufferedReader br) throws IOException {
        HttpHeader header = new HttpHeader();
        String line;
        while (!"".equals(line = br.readLine())) {
            header.add(line);
        }
        return header;
    }

    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getHeader(String key) {
        return header.get(key);
    }

    public String getParameter(String key) {
        return param.get(key);
    }
    
    public HttpCookie getCookie() {
        return header.getCookie();
    }
    
    public HttpSession getSession() {
        return header.getSession();
    }
}