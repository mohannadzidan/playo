package playo.panels;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.FlowPane;
import playo.Track;
import playo.controllers.SongCardController;
import playo.events.Change;
import playo.events.ChangeListener;
import playo.events.InvalidationListener;
import playo.logging.Logger;
import playo.playlists.Playlist;
import playo.playlists.SortedPlaylist;

import java.io.IOException;
import java.util.ArrayList;

public class SongsListPanel extends PlayOPanel {
    private static final Logger LOGGER = new Logger(SongsListPanel.class.getName());
    private final ArrayList<SongCardController> cardControllers = new ArrayList<>();
    public FlowPane songCardsContainer;
    private SortedPlaylist playlist;
    private final InvalidationListener playlistSortListener = this::invalidateAllCards;

    private final ChangeListener<Change<Track>> playlistTracksListener = (change) -> {
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
            if (match.isPresent()) {
                songCardsContainer.getChildren().remove(match.get().root);
                cardControllers.remove(match.get());
            }
        }
    };

    public void load(Playlist playlist) {
        if (this.playlist != null) {
            this.playlist.removeTrackListChangeListener(playlistTracksListener); // unbind previous list first
            this.playlist.removeOnSortListener(playlistSortListener);
        }
        this.playlist = new SortedPlaylist(playlist);
        this.playlist.addTrackListChangeListener(playlistTracksListener); // bind
        this.playlist.addOnSortListener(playlistSortListener); // bind
        invalidateAllCards();
    }

    private void invalidateAllCards() {
        var contents = songCardsContainer.getChildren();
        contents.clear();
        var tracks = this.playlist.getAllTracks();
        for (var t : tracks) {
            try {
                var cardController = SongCardController.create(t, this.playlist);
                cardController.root.setOnMouseClicked((a) -> {
                    this.playlist.moveTo(t);
                });
                contents.add(cardController.root);
                cardControllers.add(cardController);
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("Failed to create song-card");
            }
        }
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void onSortByAction(ActionEvent actionEvent) {
        MenuItem item = (MenuItem) actionEvent.getSource();
        System.out.println(item.getText());
        switch (item.getText()) {
            case "Title" -> playlist.setSortingMode(SortedPlaylist.SortingMode.TITLE);
            case "Album" -> playlist.setSortingMode(SortedPlaylist.SortingMode.ALBUM);
            case "Artist" -> playlist.setSortingMode(SortedPlaylist.SortingMode.ARTIST);
            case "Year" -> playlist.setSortingMode(SortedPlaylist.SortingMode.YEAR);
        }

    }
}
