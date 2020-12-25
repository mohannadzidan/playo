package playo.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import playo.*;

import java.io.IOException;

public class PropertyController extends PlayODynamicController {

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
