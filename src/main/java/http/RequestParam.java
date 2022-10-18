package http;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;

public class RequestParam {
    private static final Logger log = LoggerFactory.getLogger(RequestParam.class);
    private Map<String, String> params = new HashMap<String, String>();
    
    public void addQueryString(String queryString) {
        putParams(queryString);
    }

    public String get(String key) {
        return params.get(key);
    }
    
    private void putParams(String queryString) {
        log.debug("data: {}", queryString);
        if (StringUtils.isEmpty(queryString)) {
            return;
        }
        params.putAll(HttpRequestUtils.parseQueryString(queryString));
    }
}
