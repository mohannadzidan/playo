package playo;

import java.util.HashMap;

public abstract class PlayOSingletonController {
    private static final HashMap<Class<? extends PlayOSingletonController>, PlayOSingletonController> controllers = new HashMap<>();
    public PlayOSingletonController() {
        if (controllers.containsKey(getClass())) {
            throw new RuntimeException(getClass().getName() + " instantiated multiple times!");
        } else {
            controllers.put(getClass(), this);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends PlayOSingletonController> T getController(Class<T> controllerClass) {
        return (T) controllers.get(controllerClass);
    }

}
