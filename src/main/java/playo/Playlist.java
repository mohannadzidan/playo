package playo;

import playo.events.Change;
import playo.events.Event;
import playo.events.EventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Playlist {
    private final ArrayList<Track> trackList = new ArrayList<>();
    private int currentTrackIndex = 0;
    private static final Random random = new Random();
    private final Event<Change<Track>> currentTrackChangeEvent = new Event<>();
    private final Event<Change<Track>> trackListChangeEvent = new Event<>();

    public void addTrack(Track track) {
        Objects.requireNonNull(track);
        this.trackList.add(track);
        trackListChangeEvent.populate(new Change<>(null, track));
    }

    public void removeTrack(int index) {
        if (index < currentTrackIndex) {
            currentTrackIndex = hasPrevious() ? currentTrackIndex - 1 : trackList.size() - 1;
        }
        var track = getTrack(index);
        if (track != null) {
            trackListChangeEvent.populate(new Change<>(track, null));
            trackList.remove(index);
        }
    }

    public void removeTrack(Track track) {
        var index = trackList.indexOf(track);
        removeTrack(index);
    }

    public void removeTrack(String fullName) {
        int index = -1;
        for (int i = 0; i < trackList.size(); i++) {
            var track = getTrack(i);
            if (track != null && track.getURL().toString().equals(fullName)) {
                index = i;
                break;
            }
        }
        removeTrack(index);
    }

    public Track currentTrack() {
        return getTrack(currentTrackIndex);
    }

    public void nextTrack() {
        int index = hasNext() ? currentTrackIndex + 1 : 0;
        if (index != currentTrackIndex) {
            var change = new Change<>(getTrack(currentTrackIndex), getTrack(index));
            currentTrackIndex = index;
            currentTrackChangeEvent.populate(change);
        }
    }

    public void previousTrack() {
        int index = hasPrevious() ? currentTrackIndex - 1 : trackList.size() - 1;
        if (index != currentTrackIndex) {
            var change = new Change<>(getTrack(currentTrackIndex), getTrack(index));
            currentTrackIndex = index;
            currentTrackChangeEvent.populate(change);
        }
    }

    public void randomTrack() {
        // TODO BUG this shuffling will result an endless playlist
        int index = random.nextInt(trackList.size());
        var change = new Change<>(getTrack(currentTrackIndex), getTrack(index));
        currentTrackIndex = index;
        currentTrackChangeEvent.populate(change);
    }

    private Track getTrack(int index) {
        if (index >= 0 && index < trackList.size())
            return trackList.get(index);
        else return null;
    }

    public void moveTo(Track track) {
        int index = trackList.indexOf(track);
        moveTo(index);
    }

    public void moveTo(int index) {
        if (index != currentTrackIndex) {
            var change = new Change<>(getTrack(currentTrackIndex), getTrack(index));
            currentTrackIndex = index;
            currentTrackChangeEvent.populate(change);
        }
    }

    public Track[] getAllTracks() {
        Track[] tracks = new Track[trackList.size()];
        for (int i = 0; i < tracks.length; i++) {
            tracks[i] = getTrack(i);
        }
        return tracks;
    }

    public boolean hasNext() {
        return currentTrackIndex + 1 < trackList.size();
    }

    public boolean hasPrevious() {
        return currentTrackIndex - 1 >= 0;
    }

    public void addCurrentTrackChangeListener(EventListener<Change<Track>> listener) {
        currentTrackChangeEvent.addListener(listener);
        listener.onEvent(new Change<>(currentTrack(), currentTrack()));
    }

    public void removeCurrentTrackChangeListener(EventListener<Change<Track>> listener) {
        currentTrackChangeEvent.removeListener(listener);
    }

    public void addTrackListChangeListener(EventListener<Change<Track>> listener){
        trackListChangeEvent.addListener(listener);
    }

    public void removeTrackListChangeListener(EventListener<Change<Track>> listener){
        trackListChangeEvent.addListener(listener);
    }


}
