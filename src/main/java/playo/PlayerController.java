package playo;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Objects;
import java.util.function.Function;


public class PlayerController extends PlayOController {
    public Parent root;
    public Slider volumeSlider;
    public Slider timeSlider;
    public Label artistName;
    public Label trackName;
    public Label durationText;
    public Label currentTimeText;
    public ImageView musicArt;
    public CheckBox muteCheckbox;
    public CheckBox shuffleCheckbox;
    public CheckBox repeatCheckbox;
    private static PlayerController controller;
    private MediaPlayer mediaPlayer;
    private Playlist loadedList = null;
    private boolean isSeeking = false;
    private Image defaultMusicArt;

    public PlayerController() {
        if (controller != null) throw new RuntimeException("Singleton allows only one instance of this class!");
        controller = this;
    }

    public void initialize() {
        defaultMusicArt = new Image(PlayOApp.class.getResource("/icons/default-music-art.png").toString());
        musicArt.setImage(defaultMusicArt);
        timeSlider.valueChangingProperty().addListener((observableValue, oldVal, newVal) -> {
            if (!newVal) {
                if (mediaPlayer != null) mediaPlayer.seek(Duration.seconds(timeSlider.getValue()));
                isSeeking = false;
            } else {
                isSeeking = true;
            }
        });
        Function<String, Track> trackFromResources = (res) -> {
            var url = PlayOApp.class.getResource(res);
            var src = url.toString();
            return new Track(null, null, src, null, 0);
        };
        loadedList = new Playlist();
        loadedList.addTrack(trackFromResources.apply("/sounds/1.mp3"));
        loadedList.addTrack(trackFromResources.apply("/sounds/2.mp3"));
        loadTrack(loadedList.currentTrack());
    }

    private void loadTrack(Track track) {
        if (mediaPlayer != null && mediaPlayer.getStatus() != MediaPlayer.Status.DISPOSED) {
            mediaPlayer.stop();
            mediaPlayer.volumeProperty().unbind();
            mediaPlayer.muteProperty().unbind();
            mediaPlayer.setOnEndOfMedia(null);
            mediaPlayer.dispose();
        }
        mediaPlayer = new MediaPlayer(new Media(track.getFullName()));
        mediaPlayer.setOnReady(() -> {
            System.out.println("media is ready");
            timeSlider.setMax(mediaPlayer.getMedia().getDuration().toSeconds());
            durationText.setText(formatDuration(mediaPlayer.getMedia().getDuration()));
            var meta = mediaPlayer.getMedia().getMetadata();
            var keySet = meta.keySet();
            for (var k : keySet) {
                System.out.println(k + ":" + meta.get(k));
            }
            var image = (Image) meta.get("image");
            if (image != null) {
                double minDim = Math.min(image.getHeight(), image.getWidth());
                double centerX = image.getWidth() / 2, centerY = image.getHeight() / 2;
                double x = centerX - minDim / 2;
                double y = centerY - minDim / 2;
                WritableImage wi = new WritableImage(image.getPixelReader(), (int) x, (int) y, (int) minDim, (int) minDim);
                musicArt.setImage(wi);
            } else {
                musicArt.setImage(defaultMusicArt);
            }
            trackName.setText(Objects.requireNonNullElse(meta.get("title"), "Unknown").toString());
            artistName.setText(Objects.requireNonNullElse(meta.get("artist"), "Unknown").toString());
            var color = colorFromImage(musicArt.getImage()).darker().darker();
            root.setStyle("-fx-background-color:" + colorToHex(color) + ";");
        });
        mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty());
        mediaPlayer.muteProperty().bind(muteCheckbox.selectedProperty());
        mediaPlayer.currentTimeProperty().addListener((observableValue, duration, t1) -> {
            if (!isSeeking) {
                timeSlider.setValue(t1.toSeconds());
            }
            currentTimeText.setText(formatDuration(t1));
        });
        mediaPlayer.setOnEndOfMedia(() -> {
            System.out.println("end of media " + mediaPlayer.getStatus());
            if (repeatCheckbox.isSelected()) {
                loadTrack(loadedList.currentTrack());
                mediaPlayer.setAutoPlay(true);
            } else {
                if (shuffleCheckbox.isSelected()) {
                    loadTrack(loadedList.anyTrack());
                    mediaPlayer.setAutoPlay(true);
                } else if (loadedList.hasNext()) {
                    loadTrack(loadedList.nextTrack());
                    mediaPlayer.setAutoPlay(true);
                }
            }
        });
    }



    public void onPlayAction(ActionEvent actionEvent) {
        if (mediaPlayer == null) return;
        System.out.println(mediaPlayer.getStatus());
        switch (mediaPlayer.getStatus()) {
            case READY, PAUSED -> mediaPlayer.play();
            case PLAYING -> {
                if (mediaPlayer.getCurrentTime().toSeconds() >= mediaPlayer.getTotalDuration().toSeconds())
                    mediaPlayer.seek(Duration.seconds(0));
                else
                    mediaPlayer.pause();
            }

        }

    }

    public void onPreviousAction(ActionEvent actionEvent) {
        boolean isReady = mediaPlayer.getStatus().compareTo(MediaPlayer.Status.READY) >= 0
                && mediaPlayer.getStatus().compareTo(MediaPlayer.Status.STOPPED) < 0;
        if (mediaPlayer != null && isReady) {
            double totalSeconds = mediaPlayer.totalDurationProperty().getValue().toSeconds();
            double currentSeconds = mediaPlayer.currentTimeProperty().getValue().toSeconds();
            double progress = currentSeconds / totalSeconds;
            if (progress < 0.1) {
                loadTrack(loadedList.previousTrack());
                mediaPlayer.setAutoPlay(true);
            } else {
                mediaPlayer.seek(Duration.seconds(0));
            }
        } else {
            loadTrack(loadedList.previousTrack());
            mediaPlayer.setAutoPlay(true);
        }
    }

    public void onNextAction(ActionEvent actionEvent) {
        loadTrack(loadedList.nextTrack());
        mediaPlayer.setAutoPlay(true);
    }

    private static String formatDuration(Duration duration) {
        var seconds = Math.round(duration.toMinutes() % 1 * 60);
        var minutes = (int) duration.toMinutes();
        return minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }

    private static Color colorFromImage(Image image) {
        var reader = image.getPixelReader();
        double widthStep = image.getWidth()/5.0;
        double heightStep = image.getHeight()/5.0;
        double totalRed = 0, totalBlue = 0, totalGreen = 0;
        int totalPoints = 0;
        for(double x = widthStep/2; x < image.getWidth(); x+=widthStep){
            for(double y = heightStep/2; y < image.getHeight(); y+=heightStep){
                Color c = reader.getColor((int) x, (int) y);
                totalRed += c.getRed();
                totalBlue += c.getBlue();
                totalGreen += c.getGreen();
                totalPoints++;
            }
        }
       return new Color(totalRed/totalPoints, totalGreen/totalPoints, totalBlue/totalPoints, 1);
    }
    private static String colorToHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public static PlayerController getInstance() {
        return controller;
    }
}
