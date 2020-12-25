package playo.panels;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import playo.PlayOApp;
import playo.controllers.DirectoryController;

import java.io.File;
import java.io.IOException;


public class SettingsPanel extends PlayOPanel {
    @FXML
    public VBox directoriesContainer = new VBox();
    @FXML
    public RadioButton lightThemeRadioBtn = new RadioButton();
    @FXML
    public RadioButton darkThemeRadioBtn = new RadioButton();
    @FXML
    public ScrollPane directoriesList = new ScrollPane();

    public void initialize() {
        root.setVisible(false);
        ToggleGroup themeToggleGroup = new ToggleGroup();
        lightThemeRadioBtn.setSelected(true);
        darkThemeRadioBtn.setToggleGroup(themeToggleGroup);
        lightThemeRadioBtn.setToggleGroup(themeToggleGroup);
        themeToggleGroup.selectedToggleProperty().addListener((ob, oldV, newV) -> {
            var root = PlayOApp.getInstance().getPrimaryStage().getScene().getRoot();
            if (newV == lightThemeRadioBtn) {
                root.getStylesheets().remove("/layout/dark.css");
            } else {
                root.getStylesheets().add("/layout/dark.css");
            }
        });
    }

    @FXML
    public void addDirectory(MouseEvent mouseEvent) throws IOException {

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose music directory");
        File dir = chooser.showDialog(PlayOApp.getInstance().getPrimaryStage());
        if (dir != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/directory.fxml"));
            Node root = loader.load();
            DirectoryController controller = loader.getController();
            controller.text.setText(dir.toString());
            directoriesContainer.getChildren().add(root);
            PlayOApp.getInstance().getWatcher().registerAll(dir.toPath());
            controller.removeButton.setOnAction((e) -> {
                directoriesContainer.getChildren().remove(root);
                PlayOApp.getInstance().getWatcher().unRegister(dir.toPath());
            });
        }
    }
}
