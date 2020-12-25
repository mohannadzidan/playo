package playo.playlists;

import playo.events.InvalidationEvent;
import playo.events.InvalidationListener;

import java.util.Comparator;

/**
 * A playlist that is always sorted
 */
public class SortedPlaylist extends SyncedPlaylist {
    private final Comparator<String> comparator = Comparator.comparing(String::toString);
    private final InvalidationEvent listSortedEvent = new InvalidationEvent();
    private SortingMode sortingMode = SortingMode.TITLE;

    public SortedPlaylist(Playlist sync) {
        super(sync);
        this.trackList.addAll(sync.trackList);
        this.addTrackListChangeListener((change) -> sort());
        sort();
    }

    private void sort() {
        switch (sortingMode) {
            case TITLE -> this.trackList.sort((a, b) -> comparator.compare(a.getTitle(), b.getTitle()));
            case ALBUM -> this.trackList.sort((a, b) -> comparator.compare(a.getAlbum(), b.getAlbum()));
            case ARTIST -> this.trackList.sort((a, b) -> comparator.compare(a.getArtist(), b.getArtist()));
            case YEAR -> this.trackList.sort((a, b) -> comparator.compare(a.getYear(), b.getYear()));
        }

    }

    public void setSortingMode(SortingMode mode) {
        this.sortingMode = mode;
        sort();
        listSortedEvent.invoke();
    }

    public void addOnSortListener(InvalidationListener listener) {
        listSortedEvent.addListener(listener);
    }

    public void removeOnSortListener(InvalidationListener listener) {
        listSortedEvent.removeListener(listener);
    }

    public enum SortingMode {
        TITLE, ALBUM, ARTIST, YEAR
    }

}
