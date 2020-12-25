package playo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import playo.panels.PlayOPanel;
import playo.panels.PlayerPanel;
import playo.panels.SongsListPanel;
import playo.utils.MediaWatcher;

public class PlayOApp extends Application {

    private static PlayOApp app;
    private MediaWatcher watcher;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    public static PlayOApp getInstance() {
        return app;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        PlayOApp.app = this;
        this.primaryStage = primaryStage;
        watcher = new MediaWatcher();
        Parent root = FXMLLoader.load(PlayOApp.class.getResource("/layout/root.fxml"));
        primaryStage.setTitle("PlayO");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        PlayOPanel.getPanel(PlayerPanel.class).load(watcher.getPlaylist());
        PlayOPanel.getPanel(SongsListPanel.class).load(watcher.getPlaylist());

    }

    @Override
    public void stop() throws Exception {
        System.out.println("Application closed");
        watcher.shutdown();
    }

    public MediaWatcher getWatcher() {
        return watcher;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
