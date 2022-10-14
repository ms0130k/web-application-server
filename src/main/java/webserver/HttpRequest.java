package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

public class HttpRequest {

    private InputStream in;
    private RequestLine requestLine;
    private Map<String, String> headers = new HashMap<String, String>();
    private Map<String, String> params = new HashMap<String, String>();

    public HttpRequest(InputStream in) {
        this.in = in;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));) {
            String line = br.readLine();
            if (line == null) {
                return;
            }
            
//            setRequestLine(line);
            requestLine = new RequestLine(line);
            setHeaders(br);
            if ("GET".equals(getMethod())) {
                params = requestLine.getParams();
            } else {
                line = br.readLine();
                int index = Integer.parseInt(headers.get("Content-Length"));
                String queryString = line.substring(0, index);
                params = HttpRequestUtils.parseQueryString(queryString);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getMethod() throws IOException {
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
    
//    private void setRequestLine(String line) {
//        String[] requestTokens = line.split(" ");
//        method = requestTokens[0];
//        if ("GET".equals(method)) {
//            String pathAndParams = requestTokens[1];
//            String[] pathTokens = pathAndParams.split("\\?");
//            
//            path = pathTokens[0];
//            params = HttpRequestUtils.parseQueryString(pathTokens[1]);
//            return;
//        }
//        path = requestTokens[1];
//        
//    }

    private void setHeaders(BufferedReader br) throws IOException {
        String line = br.readLine();
        while (StringUtils.isNotEmpty(line)) {
            Pair pair = HttpRequestUtils.parseHeader(line);
            headers.put(pair.getKey(), pair.getValue());
            line = br.readLine();
        }
    }

}