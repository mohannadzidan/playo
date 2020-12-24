package playo;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.stage.Stage;
import javafx.scene.text.Text.*;
import javafx.scene.text.*;
import javafx.scene.control.ToggleGroup;

import java.io.IOException;

public class PlayOApp extends Application {


    private static PlayOApp app;
    SettingsWindow settingsWindow = new SettingsWindow();

    public PlayOApp() throws IOException {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/settings.fxml"));
        Parent root = loader.load();
        settingsWindow = loader.getController();
        settingsWindow.lightMode.setSelected(true);
        ToggleGroup theme = new ToggleGroup();
        settingsWindow.lightMode.setToggleGroup(theme);
        settingsWindow.darkMode.setToggleGroup(theme);
        theme.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            public void changed(ObservableValue<? extends Toggle> ob,
                                Toggle o, Toggle n)
            {
                RadioButton rb = (RadioButton)theme.getSelectedToggle();
                if (rb == settingsWindow.lightMode) {
                    root.getStylesheets().remove("/layout/dark.css");
                }
                else {
                    root.getStylesheets().add("/layout/dark.css");
                }
            }
        });
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
