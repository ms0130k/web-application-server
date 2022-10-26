package http;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpSessions {
    public static final String SESSION_ID_NAME = "JSESSIONID";
    private static Map<String, HttpSession> sessions = new HashMap<String, HttpSession>();
    
    public static HttpSession get(String id) {
        HttpSession session = sessions.get(id);
        if (session == null) {
            session = new HttpSession(id);
            sessions.put(id, session);
        }
        return session;
    }
    
    public static void remove(String id) {
        sessions.remove(id);
    }
}
