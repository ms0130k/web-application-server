package http;

import java.util.Map;

import util.HttpRequestUtils;

public class HttpCookie {
    private Map<String, String> cookies;

    HttpCookie(String cookieValue) {
        cookies = HttpRequestUtils.parseCookies(cookieValue);
    }
    
    public String get(String key) {
        return cookies.get(key);
    }
}
