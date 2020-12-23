package playo.views;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;

public class BongoButton extends HBox {

    private Background activeBackground;
    @FXML
    private ImageView icon;
    @FXML
    private Label title;
    private final ColorAdjust colorAdjust = new ColorAdjust();
    public BongoButton() {
        super();
        VBox.setVgrow(this, Priority.ALWAYS);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/bongo-button.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        icon.setEffect(colorAdjust);

    }


    public ImageView getIcon() {
        return icon;
    }

    public Label getTitle() {
        return title;
    }
    public ColorAdjust getIconColorAdjust(){
        return colorAdjust;
    }
}
