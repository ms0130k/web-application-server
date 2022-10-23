package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.IOUtils;

public class HttpRequest {
    private final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private RequestLine requestLine;
    private HttpHeader header = new HttpHeader();
    private RequestParam param = new RequestParam();
    private HttpCookie cookie;

    public HttpRequest(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            requestLine = new RequestLine(createRequestLine(br));
            param.addQueryString(requestLine.getQueryString());
            processHeaders(br);
            param.addQueryString(IOUtils.readData(br, header.getContentLength()));
            setCookies();

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

    private void setCookies() {
        String cookieValue = getHeader("Cookie");
        if (StringUtils.isNotBlank(cookieValue)) {
            cookie = new HttpCookie(cookieValue);
        }
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

    public String getCookie(String key) {
        return cookie.get(key);
    }
    
    public boolean isLogined() {
        return Boolean.parseBoolean(cookie.get("logined"));
    }

    private void processHeaders(BufferedReader br) throws IOException {
        String line;
        while (!"".equals(line = br.readLine())) {
            header.add(line);
        }
    }

}