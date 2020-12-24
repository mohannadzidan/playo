package playo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class SettingsWindow {
    @FXML
    public VBox directory = new VBox();
    @FXML
    public RadioButton lightThemeRadioBtn = new RadioButton();
    @FXML
    public RadioButton darkThemeRadioBtn = new RadioButton();
    @FXML
    public ScrollPane directoriesList = new ScrollPane();
    ArrayList<File> cars = new ArrayList<>();


    public void initialize() {
        ToggleGroup themeToggleGroup = new ToggleGroup();
        lightThemeRadioBtn.setSelected(true);
        darkThemeRadioBtn.setToggleGroup(themeToggleGroup);
        lightThemeRadioBtn.setToggleGroup(themeToggleGroup);
        themeToggleGroup.selectedToggleProperty().addListener((ob, oldV, newV) -> {
            if (newV == lightThemeRadioBtn) {
                // TODO access the root of the window
                // root.getStylesheets().remove("/layout/dark.css");
            } else {
                // TODO access the root of the window
                // root.getStylesheets().add("/layout/dark.css");
            }
        });
    }

    @FXML
    public void addDirectory(MouseEvent mouseEvent) throws IOException {

        JFileChooser chooser = new JFileChooser(); // TODO replace this with javafx DirectoryChooser
        chooser.setCurrentDirectory(new File("."));
        chooser.setDialogTitle("choosertitle");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/directory.fxml"));
            Node root = loader.load();
            DirectoryController dir = loader.getController();
            dir.text.setText(String.valueOf(chooser.getSelectedFile()));
            directoriesList.setContent(directory);
            directoriesList.setPannable(true);
            directory.getChildren().add(root);
            cars.add((chooser.getSelectedFile()));
            dir.removeButton.setOnAction((e) -> {
                directory.getChildren().remove(root);
            });

        } else {
            System.out.println("No Selection ");
        }


    }

    @FXML
    public void lightModeClick(MouseEvent mouseEvent) {

    }

    public void darkModeClick(MouseEvent mouseEvent) {
    }

    public void hdel(ActionEvent actionEvent) {
    }
}
