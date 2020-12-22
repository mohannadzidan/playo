package playo;

import javafx.scene.layout.FlowPane;
import playo.events.Change;
import playo.events.EventListener;
import playo.logging.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class SongsListController extends PlayOSingletonController implements Loader<Playlist> {
    public FlowPane songCardsContainer;

    private static final Logger LOGGER = new Logger(SongsListController.class.getName());
    private Playlist playlist;
    private final ArrayList<SongCardController> cardControllers = new ArrayList<>();

    private final EventListener<Change<Track>> playlistTracksListener = (change) -> {
        if (change.getOldValue() == null && change.getNewValue() != null) {
            // track added
            var track = change.getNewValue();
            try {
                var cardController = SongCardController.create(track, playlist);
                cardController.root.setOnMouseClicked((a) -> {
                    playlist.moveTo(track);
                });
                songCardsContainer.getChildren().add(cardController.root);
                cardControllers.add(cardController);
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("Failed to create song-card");
            }
        } else if (change.getOldValue() != null && change.getNewValue() == null) {
            // track removed
            var track = change.getOldValue();
            var match = cardControllers.stream().filter((a) -> a.getTrack() == change.getOldValue()).findFirst();
            if(match.isPresent()){
                songCardsContainer.getChildren().remove(match.get().root);
                cardControllers.remove(match.get());
            }
        }
    };

    @Override
    public void load(Playlist playlist) {
        if (this.playlist != null) {
            // unbind previous list first

        }
        this.playlist = playlist;
        var contents = songCardsContainer.getChildren();
        contents.clear();
        var tracks = playlist.getAllTracks();
        for (var t : tracks) {
            try {
                var cardController = SongCardController.create(t, playlist);
                cardController.root.setOnMouseClicked((a) -> {
                    playlist.moveTo(t);
                });
                contents.add(cardController.root);
                cardControllers.add(cardController);
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("Failed to create song-card");
            }
        }
        // bind
        playlist.addTrackListChangeListener(playlistTracksListener);
    }

    public Playlist getPlaylist() {
        return playlist;
    }
}
