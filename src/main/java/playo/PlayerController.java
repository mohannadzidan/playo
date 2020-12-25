package playo;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import playo.events.Change;
import playo.events.ChangeEvent;
import playo.events.ChangeListener;
import playo.playlists.Playlist;
import playo.utils.ImageUtils;
import playo.views.SwingingLabel;


public class PlayerController extends PlayOSingletonController implements Loader<Playlist> {
    public Parent root;
    public VBox trackInfoContainer;
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
    public ImageView background;

    private MediaPlayer mediaPlayer;
    private Playlist loadedList = null;
    private boolean isSliderModifiedFromPlayer;

    private final ChangeListener<Change<Track>> trackChangeListener = (c) -> {
        boolean check = mediaPlayer != null;
        loadTrack(c.getNewValue());
        if (check) mediaPlayer.setAutoPlay(true);
    };

    private final ChangeEvent<Change<MediaPlayer.Status>> playerStateEvent = new ChangeEvent<>();

    public void initialize() {
        musicArt.setImage(Track.DEFAULT_MUSIC_ART);
        musicArt.setViewport(ImageUtils.aspectRatioViewport(Track.DEFAULT_MUSIC_ART, 1, 1));

        timeSlider.valueProperty().addListener((o, oldV, newV) -> {
            if (isSliderModifiedFromPlayer) {
                isSliderModifiedFromPlayer = false;
            } else {
                mediaPlayer.seek(Duration.millis(newV.doubleValue()));
            }
        });

        BoxBlur blur = new BoxBlur();
        blur.setIterations(3);
        blur.setHeight(10);
        blur.setWidth(10);
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setInput(blur);
        colorAdjust.setBrightness(-0.5);
        background.setEffect(colorAdjust);
        Rectangle clip = new Rectangle();
        root.setClip(clip);
        root.layoutBoundsProperty().addListener((o, oldVal, newVal) -> {
            clip.setWidth(newVal.getWidth());
            clip.setHeight(newVal.getHeight());
            if (background.getImage() != null) {
                double scale = Math.max(newVal.getWidth() / background.getImage().getWidth(), newVal.getHeight() / background.getImage().getHeight()) + 0.1f;
                background.setScaleX(scale);
                background.setScaleY(scale);
                background.setScaleZ(scale);
            }
        });
        background.imageProperty().addListener((o, oldVal, newVal) -> {
            var bounds = root.getLayoutBounds();
            background.setFitWidth(newVal.getWidth());
            background.setFitHeight(newVal.getHeight());
            double scale = Math.max(bounds.getWidth() / newVal.getWidth(), bounds.getHeight() / newVal.getHeight()) + 0.1f;
            background.setScaleX(scale);
            background.setScaleY(scale);
            background.setScaleZ(scale);
        });
        setupAnimations();
    }

    private void setupAnimations() {
        SwingingLabel.convertLabel(trackName, 60, false);
    }

    private void loadTrack(Track track) {
        if (mediaPlayer != null && mediaPlayer.getStatus() != MediaPlayer.Status.DISPOSED) {
            mediaPlayer.volumeProperty().unbind();
            mediaPlayer.muteProperty().unbind();
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        if (track == null) return;
        mediaPlayer = new MediaPlayer(new Media(track.getURL().toString()));
        mediaPlayer.setOnReady(() -> {
            timeSlider.setMax(mediaPlayer.getTotalDuration().toMillis());
            durationText.setText(formatDuration(mediaPlayer.getMedia().getDuration()));
            musicArt.setImage(track.getMusicArt());
            musicArt.setViewport(ImageUtils.aspectRatioViewport(musicArt.getImage(), 1, 1));
            trackName.setText(track.getTitle());
            artistName.setText(track.getArtist());
            background.setImage(track.getMusicArt());
        });
        mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty());
        mediaPlayer.muteProperty().bind(muteCheckbox.selectedProperty());
        mediaPlayer.statusProperty().addListener((o, oldData, newData) -> playerStateEvent.invoke(new Change<>(oldData, newData)));

        mediaPlayer.currentTimeProperty().addListener((observableValue, duration, t1) -> {
            isSliderModifiedFromPlayer = true;
            timeSlider.setValue(t1.toMillis());
            currentTimeText.setText(formatDuration(t1));
        });
        mediaPlayer.setOnEndOfMedia(() -> {
            if (repeatCheckbox.isSelected()) {
                mediaPlayer.seek(Duration.millis(0));
            } else {
                if (shuffleCheckbox.isSelected()) {
                    loadedList.randomTrack();
                } else if (loadedList.hasNext()) {
                    loadedList.nextTrack();
                }
            }
        });
    }


    public void onPlayAction(ActionEvent actionEvent) {
        if (mediaPlayer == null) return;

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
        if (mediaPlayer == null) loadedList.previousTrack();
        if (loadedList.currentTrack() == null) return;
        boolean isReady = mediaPlayer.getStatus().compareTo(MediaPlayer.Status.READY) >= 0
                && mediaPlayer.getStatus().compareTo(MediaPlayer.Status.STOPPED) < 0;
        if (isReady) {
            double totalSeconds = mediaPlayer.totalDurationProperty().getValue().toSeconds();
            double currentSeconds = mediaPlayer.currentTimeProperty().getValue().toSeconds();
            double progress = currentSeconds / totalSeconds;
            if (progress < 0.1) {
                loadedList.previousTrack();
            } else {
                mediaPlayer.seek(Duration.seconds(0));
            }
        } else {
            loadedList.previousTrack();
        }
    }

    public void onNextAction(ActionEvent actionEvent) {
        loadedList.nextTrack();
    }

    private static String formatDuration(Duration duration) {
        var seconds = Math.round(duration.toMinutes() % 1 * 60);
        var minutes = (int) duration.toMinutes();
        return minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }


    private static String colorToHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    @Override
    public void load(Playlist playlist) {
        if (loadedList != playlist) {
            if (loadedList != null)
                loadedList.removeCurrentTrackChangeListener(trackChangeListener);
            this.loadedList = playlist;
            playlist.addCurrentTrackChangeListener(trackChangeListener);
        }
    }

    public void addPlayerStateListener(ChangeListener<Change<MediaPlayer.Status>> listener) {
        playerStateEvent.addListener(listener);
    }

    public void removePlayerStateListener(ChangeListener<Change<MediaPlayer.Status>> listener) {
        playerStateEvent.removeListener(listener);
    }

}
