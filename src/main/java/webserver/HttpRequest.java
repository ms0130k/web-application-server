package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;
import util.IOUtils;
import util.HttpRequestUtils.Pair;

public class HttpRequest {
    private final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private RequestLine requestLine;
    private Map<String, String> headers = new HashMap<String, String>();
    private Map<String, String> params = new HashMap<String, String>();
    private Map<String, String> cookies = new HashMap<String, String>();

    public HttpRequest(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line = br.readLine();
            if (line == null) {
                return;
            }

            requestLine = new RequestLine(line);
            setHeaders(br);
            setCookies();

            if (getMethod().isGet()) {
                params = requestLine.getParams();
            } else {
                int contentLength = Integer.parseInt(headers.get("Content-Length"));
                String queryString = IOUtils.readData(br, contentLength);
                params = HttpRequestUtils.parseQueryString(queryString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCookies() {
        String cookie = getHeader("Cookie");
        if (StringUtils.isNotBlank(cookie)) {
            cookies = HttpRequestUtils.parseCookies(cookie);
        }
    }

    public HttpMethod getMethod() throws IOException {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getParameter(String key) {
        return params.get(key);
    }

    public String getCookie(String key) {
        return cookies.get(key);
    }
    
    public boolean isLogined() {
        return Boolean.parseBoolean(cookies.get("logined"));
    }


    private void setHeaders(BufferedReader br) throws IOException {
        String line;
        while (!"".equals(line = br.readLine())) {
            log.debug("header: {}", line);
            Pair pair = HttpRequestUtils.parseHeader(line);
            headers.put(pair.getKey(), pair.getValue());
        }
    }

}