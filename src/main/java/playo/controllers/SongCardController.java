package playo.controllers;

import javafx.animation.*;
import javafx.beans.InvalidationListener;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import playo.PlayOApp;
import playo.Track;
import playo.events.Change;
import playo.events.ChangeListener;
import playo.logging.Logger;
import playo.panels.PlayOController;
import playo.panels.PlayOPanel;
import playo.panels.PlayerPanel;
import playo.playlists.Playlist;
import playo.utils.ImageUtils;
import playo.views.SwingingLabel;

import java.io.IOException;
import java.util.stream.Stream;

public class SongCardController extends PlayOController {
    private static final Logger LOGGER = new Logger(SongCardController.class.getName());
    public Label trackName;
    public Label artistName;
    public ImageView art;
    public VBox stateOverlay;
    public ImageView playIcon;
    public ImageView pauseIcon;
    private final ChangeListener<Change<MediaPlayer.Status>> playerStatusListener = (c) -> {
        if (c.getNewValue() == MediaPlayer.Status.PLAYING) {
            playIcon.setVisible(true);
            pauseIcon.setVisible(false);
        } else {
            playIcon.setVisible(false);
            pauseIcon.setVisible(true);
        }
    };
    public VBox cardInfoContainer;
    private final MapChangeListener<String, Object> metadataLoadListener = (o) -> {
        if (o.wasAdded()) {
            switch (o.getKey()) {
                case "title" -> trackName.setText((String) o.getValueAdded());
                case "artist" -> artistName.setText((String) o.getValueAdded());
                case "image" -> {
                    art.setImage((Image) o.getValueAdded());
                    art.setViewport(ImageUtils.aspectRatioViewport(art.getImage(), 1, 1));
                }
            }
            if (Stream.of("album", "year", "comment", "track").anyMatch(s -> o.getKey().equals(s))) {
                try {
                    var info = PropertyController.create(o.getKey(), o.getValueAdded().toString());
                    SwingingLabel.convertLabel(info.value, 60, false);
                    cardInfoContainer.getChildren().add(info.root);
                } catch (IOException e) {
                    e.printStackTrace();
                    LOGGER.error("Couldn't create PropertyController!");
                }
            }

        }

    };
    private boolean isSetup = false;
    private Track track;
    private Playlist playlist;
    private FadeTransition fadeStateOverlayAnimation;
    private final ChangeListener<Change<Track>> trackChangeListener = (c) -> {
        if (this.track != null && this.track == c.getNewValue()) {
            var player = PlayOPanel.getPanel(PlayerPanel.class);
            player.addPlayerStateListener(playerStatusListener);
            fadeStateOverlayAnimation.setRate(-1);
            fadeStateOverlayAnimation.play();
        } else if (this.track != null && c.getOldValue() == this.track) {
            var player = PlayOPanel.getPanel(PlayerPanel.class);
            player.removePlayerStateListener(playerStatusListener);
            fadeStateOverlayAnimation.setRate(1);
            fadeStateOverlayAnimation.play();
        }
    };
    private Timeline hoverAnimation;

    public static SongCardController create(Track track, Playlist playlist) throws IOException {
        var loader = new FXMLLoader();
        loader.load(PlayOApp.class.getResource("/layout/song-card.fxml").openStream());
        var cardController = (SongCardController) loader.getController();
        cardController.setup(track, playlist);
        return cardController;
    }

    public void initialize() {


        root.parentProperty().addListener((a, b, c) -> {
            if (b != null && c == null) {
                dispose();
            }
        });
    }

    private void setupAnimations() {
        // setup state overlay animations
        fadeStateOverlayAnimation = new FadeTransition(Duration.millis(500), stateOverlay);
        fadeStateOverlayAnimation.setFromValue(1);
        fadeStateOverlayAnimation.setToValue(0);
        // set on hover animation
        hoverAnimation = new Timeline();
        InvalidationListener setupHoverAnimation = a -> {
            var requiredHeight = art.getFitHeight() - cardInfoContainer.getHeight();
            var hRatio = Math.max(0.01f, requiredHeight / art.getFitHeight());
            hoverAnimation.getKeyFrames().clear();
            for (int i = 0; i <= 30; i++) {
                double frameRatio = i / 30.0;
                hoverAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(frameRatio * 200), new KeyValue(
                        art.viewportProperty(),
                        ImageUtils.aspectRatioViewport(art.getImage(), 1, 1 + (hRatio - 1) * frameRatio),
                        Interpolator.DISCRETE)));
            }
            hoverAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(0), new KeyValue(
                    cardInfoContainer.opacityProperty(),
                    0,
                    Interpolator.LINEAR)));
            hoverAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(200), new KeyValue(
                    cardInfoContainer.opacityProperty(),
                    0,
                    Interpolator.LINEAR)));
            hoverAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(400), new KeyValue(
                    cardInfoContainer.opacityProperty(),
                    1,
                    Interpolator.LINEAR)));
        };
        art.imageProperty().addListener(setupHoverAnimation);
        cardInfoContainer.heightProperty().addListener(setupHoverAnimation);
        root.setOnMouseEntered((e) -> {
            hoverAnimation.setRate(1);
            hoverAnimation.play();
        });
        root.setOnMouseExited((e) -> {
            hoverAnimation.setRate(-1);
            hoverAnimation.play();
        });
        SwingingLabel.convertLabel(trackName, 60, true);
    }

    public void setup(Track track, Playlist playlist) {
        if (isSetup) throw new RuntimeException("Cannot setup card twice!");
        isSetup = true;
        this.track = track;
        this.playlist = playlist;
        setupAnimations();
        trackName.setText(track.getTitle());
        artistName.setText(track.getArtist());
        art.setImage(track.getMusicArt());
        art.setViewport(ImageUtils.aspectRatioViewport(track.getMusicArt(), 1, 1));
        playlist.addCurrentTrackChangeListener(trackChangeListener);
        track.getMetadata().addListener(metadataLoadListener);
    }

    @Override
    public void dispose() {
        if (!isSetup) return;
        LOGGER.info("disposed card " + hashCode());
        track.getMetadata().removeListener(metadataLoadListener);
        playlist.removeCurrentTrackChangeListener(trackChangeListener);
    }

    public Track getTrack() {
        return track;
    }
}
