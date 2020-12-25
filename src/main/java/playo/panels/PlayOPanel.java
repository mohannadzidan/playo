package playo.panels;

import javafx.scene.Parent;
import playo.PlayOApp;

import java.util.HashMap;

public abstract class PlayOPanel {
    private static final HashMap<Class<? extends PlayOPanel>, PlayOPanel> controllers = new HashMap<>();
    private static boolean shownListenerAdded = false;
    private static boolean stageShowEventInvoked = false;
    public Parent root;

    public PlayOPanel() {
        if (stageShowEventInvoked)
            throw new RuntimeException("PlayOController cannot be instantiated after the primary stage shown!");
        if (controllers.containsKey(getClass())) {
            throw new RuntimeException(getClass().getName() + " instantiated multiple times!");
        } else {
            controllers.put(getClass(), this);
        }
        if (!shownListenerAdded) {
            shownListenerAdded = true;
            PlayOApp.getInstance().getPrimaryStage().setOnShown((event) -> {
                for (var controller : controllers.values()) {
                    controller.onShowInternally();
                }
                stageShowEventInvoked = true;
            });
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends PlayOPanel> T getPanel(Class<T> panelClass) {
        return (T) controllers.get(panelClass);
    }

    private void onShowInternally() {
        root.managedProperty().bind(root.visibleProperty());
        onShow();
    }

    protected void onShow() {
    }

}
