package playo.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import playo.PlayOSingletonController;
import playo.views.BongoButton;

import java.io.IOException;

public class SidePanelController extends PlayOSingletonController {
    public BongoButton allTracksButton;
    public BongoButton favouriteTracksButton;
    public BongoButton settingsButton;
    public BongoButton selected;
    private final EventHandler<MouseEvent> bongoClickListener = (event) -> {
        if(selected!= null) {
            selected.getIconColorAdjust().setBrightness(0.5);
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
        selected.getIconColorAdjust().setBrightness(1);
    };

    public void initialize() {
        allTracksButton.getTitle().setText("All");
        favouriteTracksButton.getTitle().setText("Favourite");
        settingsButton.getTitle().setText("Settings");
        try {
            allTracksButton.getIcon().setImage(new Image(getClass().getResource("/icons/cassette-black.png").openStream()));
            favouriteTracksButton.getIcon().setImage(new Image(getClass().getResource("/icons/like-black.png").openStream()));
            settingsButton.getIcon().setImage(new Image(getClass().getResource("/icons/settings-black.png").openStream()));
            allTracksButton.getIconColorAdjust().setBrightness(0.5);
            favouriteTracksButton.getIconColorAdjust().setBrightness(0.5);
            settingsButton.getIconColorAdjust().setBrightness(0.5);
            allTracksButton.getTitle().setAlignment(Pos.CENTER_LEFT);
            favouriteTracksButton.getTitle().setAlignment(Pos.CENTER_LEFT);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        allTracksButton.setOnMouseClicked(bongoClickListener);
        favouriteTracksButton.setOnMouseClicked(bongoClickListener);
        settingsButton.setOnMouseClicked(bongoClickListener);
    }
}
