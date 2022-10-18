package webserver;

import java.util.HashMap;
import java.util.Map;

import controller.Controller;
import controller.ControllerLogin;
import controller.ControllerUserCreation;
import controller.ControllerUserList;

public class RequestMapping {

    private static Map<String, Controller> controllers = new HashMap<String, Controller>();
    
    static {
        controllers.put("/user/create", new ControllerUserCreation());
        controllers.put("/user/login", new ControllerLogin());
        controllers.put("/user/list", new ControllerUserList());
    }

    public static Controller getController(String requestUrl) {
        return controllers.get(requestUrl);
    }
}
