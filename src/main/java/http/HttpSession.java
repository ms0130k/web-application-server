package http;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {
    private static Map<String, Object> values = new HashMap<String, Object>();
    
    private String id;

    protected HttpSession(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
    
    public void setAttribute(String key, Object value) {
        values.put(key, value);
    }
    
    public Object getAttrubute(String key) {
        return values.get(key);
    }
    
    public void removeAttribute(String key) {
        values.remove(key);
    }
    
    public void invalidate() {
        HttpSessions.remove(id);
    }
}
