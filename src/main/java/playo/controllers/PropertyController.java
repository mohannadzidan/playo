package playo.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import playo.PlayOApp;
import playo.panels.PlayOController;

import java.io.IOException;

public class PropertyController extends PlayOController {

    public Label title;
    public Label value;

    public static PropertyController create(String title, String value) throws IOException {
        var loader = new FXMLLoader();
        loader.load(PlayOApp.class.getResource("/layout/property.fxml").openStream());
        PropertyController controller = loader.getController();
        controller.title.setText(title);
        controller.value.setText(value);
        return controller;
    }

    @Override
    public void dispose() {

    }
}
