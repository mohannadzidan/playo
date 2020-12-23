package playo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import playo.utils.MediaWatcher;

import java.io.File;

public class PlayOApp extends Application {


    private static PlayOApp app;
    private MediaWatcher watcher;

    @Override
    public void start(Stage primaryStage) throws Exception {
        PlayOApp.app = this;
        Parent root = FXMLLoader.load(PlayOApp.class.getResource("/layout/root.fxml"));
        primaryStage.setTitle("PlayO");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setScene(new Scene(root, 1280, 720));
        //primaryStage.getScene().getStylesheets().add("/css/dark-theme.css");
        primaryStage.show();
        var file = new File("C:/Users/mohan/Music");
        System.out.println(file.exists());
        watcher = new MediaWatcher();
        watcher.registerAll(file.toPath());
        PlayOSingletonController.getController(PlayerController.class).load(watcher.getPlaylist());
        PlayOSingletonController.getController(SongsListController.class).load(watcher.getPlaylist());
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Application closed");
        watcher.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static PlayOApp getInstance() {
        return app;
    }
}
