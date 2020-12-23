package playo.controllers;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import playo.PlayOSingletonController;

import java.io.IOException;

public class SidePanelController extends PlayOSingletonController {
    public Button allTracksButton;
    public Button favouriteTracksButton;
    public Button settingsButton;
    public Button selected;
    private final EventHandler<MouseEvent> buttonsClickListener = (event) -> {
        if(selected!= null) {
            selected.getStyleClass().remove("selected");
        }

        if (event.getSource() == allTracksButton) {
            selected = allTracksButton;
            // TODO load all tracks playlist
        } else if (event.getSource() == favouriteTracksButton) {
            selected = favouriteTracksButton;

            // TODO load favourite tracks playlist
        } else {
            selected = settingsButton;
            // TODO show settings panel
        }
        selected.getStyleClass().add("selected");
    };

    public void initialize() {
        /*try {
            allTracksButton.getIcon().setImage(new Image(getClass().getResource("/icons/cassette-black.png").openStream()));
            favouriteTracksButton.getIcon().setImage(new Image(getClass().getResource("/icons/like-black.png").openStream()));
            settingsButton.getIcon().setImage(new Image(getClass().getResource("/icons/settings-black.png").openStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        allTracksButton.setOnMouseClicked(buttonsClickListener);
        favouriteTracksButton.setOnMouseClicked(buttonsClickListener);
        settingsButton.setOnMouseClicked(buttonsClickListener);
    }
}
