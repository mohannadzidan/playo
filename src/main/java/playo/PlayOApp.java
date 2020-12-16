package playo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PlayOApp extends Application {


    private static PlayOApp app;

    @Override
    public void start(Stage primaryStage) throws Exception {
        PlayOApp.app = this;
        var loader = new FXMLLoader();
        Parent root = (Parent) loader.load(PlayOApp.class.getResource("/layout/root.fxml"));
        primaryStage.setTitle("PlayO");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

    public static PlayOApp getInstance() {
        return app;
    }
}
