package webserver;

import java.util.HashMap;
import java.util.Map;

import controller.Controller;
import controller.LoginController;
import controller.UserCreationController;
import controller.UserListController;

public class RequestMapping {

    private static Map<String, Controller> controllers = new HashMap<String, Controller>();
    
    static {
        controllers.put("/user/create", new UserCreationController());
        controllers.put("/user/login", new LoginController());
        controllers.put("/user/list", new UserListController());
    }

    public static Controller getController(String requestUrl) {
        return controllers.get(requestUrl);
    }
}
