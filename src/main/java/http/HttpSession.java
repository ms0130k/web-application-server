package http;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {
    private static Map<String, HttpSession> sessions = new HashMap<String, HttpSession>();

    public static HttpSession get(String id) {
        HttpSession session = sessions.get(id);
        
        if (session == null) {
            session = new HttpSession();
            sessions.put(id, session);
        }
        return session;
    }
    
    static void remove(String id) {
        sessions.remove(id);
    }
}
