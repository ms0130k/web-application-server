package http;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

public class HttpHeader {
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final Logger log = LoggerFactory.getLogger(HttpHeader.class);
    private Map<String, String> headers = new HashMap<String, String>();

    void add(String value) {
        log.debug("header: {}", value);
        Pair pair = HttpRequestUtils.parseHeader(value);
        headers.put(pair.getKey(), pair.getValue());
    }

    String get(String key) {
        return headers.get(key);
    }
    
    int getContentLength() {
        return getInt(CONTENT_LENGTH);
    }

    int getInt(String key) {
        String header = get(key);
        return header == null ? 0 : Integer.parseInt(header);
    }
}
