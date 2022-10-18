package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestLine {
    private static final Logger log = LoggerFactory.getLogger(RequestLine.class);
    private HttpMethod method;
    private String path;
    private String queryString;

    public RequestLine(String line) {
        log.debug("request line: {}", line);
        String[] tokens = line.split(" ");
        method = HttpMethod.valueOf(tokens[0]);

        String[] uri = tokens[1].split("\\?");
        path = uri[0];
        if (uri.length == 2) {
            queryString = uri[1];
        }

    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getQueryString() {
        return queryString;
    }
}
