package playo.controllers;

import javafx.scene.control.Button;
import playo.panels.PlayOPanel;
import playo.panels.SettingsPanel;
import playo.panels.SongsListPanel;

public class SidePanel extends PlayOPanel {
    public Button allTracksButton;
    public Button favouriteTracksButton;
    public Button settingsButton;
    private Button selected;

    public void initialize() {
        /*try {
            allTracksButton.getIcon().setImage(new Image(getClass().getResource("/icons/cassette-black.png").openStream()));
            favouriteTracksButton.getIcon().setImage(new Image(getClass().getResource("/icons/like-black.png").openStream()));
            settingsButton.getIcon().setImage(new Image(getClass().getResource("/icons/settings-black.png").openStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

    }

    @Override
    protected void onShow() {
        var settings = PlayOPanel.getPanel(SettingsPanel.class);
        var songsList = PlayOPanel.getPanel(SongsListPanel.class);
        allTracksButton.setOnMouseClicked((e) -> {
            if (selected != null) {
                selected.getStyleClass().remove("selected");
            }
            selected = allTracksButton;
            settings.root.setVisible(false);
            songsList.root.setVisible(true);
            selected.getStyleClass().add("selected");
        });
        settingsButton.setOnMouseClicked((e) -> {
            if (selected != null) {
                selected.getStyleClass().remove("selected");
            }
            selected = allTracksButton;
            settings.root.setVisible(true);
            songsList.root.setVisible(false);
            selected.getStyleClass().add("selected");
        });
    }
}
