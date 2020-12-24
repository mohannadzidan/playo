package playo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;
import javafx.scene.control.ScrollPane;
import javafx.fxml.FXMLLoader;
import java.util.ArrayList;


public class SettingsWindow {
    @FXML
    public VBox directoryy = new VBox();
    @FXML
    public RadioButton lightMode = new RadioButton();
    @FXML
    public RadioButton darkMode = new RadioButton();
    @FXML

    public ScrollPane directoriesList = new ScrollPane();
    ArrayList<File> cars = new ArrayList<File>();
    public SettingsWindow() throws IOException {
    }

    @FXML
    public void addDirClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/Directory.fxml"));
        Node root = loader.load();
        Directory dir = loader.getController();
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setDialogTitle("choosertitle");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            dir.diectory.setText(String.valueOf(chooser.getSelectedFile()));
            directoriesList.setContent(directoryy);
            directoriesList.setPannable(true);
            directoryy.getChildren().add(root);
            cars.add((chooser.getSelectedFile()));
        }
        else {
            System.out.println("No Selection ");
        }
        dir.removeDir.onMouseClickedProperty().addListener((a)->{
            directoryy.getChildren().remove(root);
        });

    }
    @FXML
    public void lightModeClick(MouseEvent mouseEvent) {

    }

    public void darkModeClick(MouseEvent mouseEvent) {
    }

    public void hdel(ActionEvent actionEvent) {
    }
}
