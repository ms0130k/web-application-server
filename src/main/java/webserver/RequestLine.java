package webserver;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;

public class RequestLine {
    private static final Logger log = LoggerFactory.getLogger(RequestLine.class);
    private String method;
    private String path;
    private Map<String, String> params;
    public RequestLine(String line) {
        String[] tokens = line.split(" ");
        method = tokens[0];
        if (!"GET".equals(method)) {
            path = tokens[1];
            return;
        }
        int index = tokens[1].indexOf("?");
        if (index == -1) {
            path = tokens[1];
        } else {
            path = tokens[1].substring(index);
            params = HttpRequestUtils.parseQueryString(tokens[1].substring(index + 1));
        }
    }
    public String getMethod() {
        return method;
    }
    public String getPath() {
        return path;
    }
    public Map<String, String> getParams() {
        return params;
    }
}
