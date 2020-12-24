package playo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.IOException;

public class Directory {
    @FXML
    public HBox directoryBox = new HBox();
    @FXML
    public Text diectory = new Text();
    @FXML
    public Button removeDir = new Button();
    SettingsWindow settingsWindow = new SettingsWindow();

    public Directory() throws IOException {
    }

    @FXML
    public void removeClick(ActionEvent actionEvent) {
     //   directoryBox.getChildren().clear();
    }
}
