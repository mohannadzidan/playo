package playo.playlists;

import playo.Track;
import playo.events.Change;
import playo.events.ChangeEvent;
import playo.events.ChangeListener;
import playo.logging.Logger;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Playlist {
    private static final Random random = new Random();
    protected final ArrayList<Track> trackList = new ArrayList<>();
    private final ChangeEvent<Change<Track>> currentTrackChangeEvent = new ChangeEvent<>();
    private final ChangeEvent<Change<Track>> trackListChangeEvent = new ChangeEvent<>();
    protected int currentTrackIndex = -1;

    public void addTrack(Track track) {
        Objects.requireNonNull(track);
        this.trackList.add(track);
        trackListChangeEvent.invoke(new Change<>(null, track));
    }

    public void removeTrack(int index) {
        if (index < currentTrackIndex) {
            currentTrackIndex = hasPrevious() ? currentTrackIndex - 1 : trackList.size() - 1;
        }
        var track = getTrack(index);
        if (track != null) {
            trackList.remove(index);
            trackListChangeEvent.invoke(new Change<>(track, null));
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
        moveTo(index);
    }

    public void previousTrack() {
        int index = hasPrevious() ? currentTrackIndex - 1 : trackList.size() - 1;
        moveTo(index);
    }

    public void randomTrack() {
        // TODO BUG this shuffling will result an endless playlist
        int index = random.nextInt(trackList.size());
        moveTo(index);
    }

    protected Track getTrack(int index) {
        if (index >= 0 && index < trackList.size())
            return trackList.get(index);
        else return null;
    }

    public int getTrackIndex(Track track) {
        return this.trackList.indexOf(track);
    }

    public void moveTo(Track track) {
        int index = trackList.indexOf(track);
        moveTo(index);
    }

    public void moveTo(int index) {
        if (index < 0 || index >= trackList.size()) {
            Logger.error(this.getClass().getName(), "Attempt move to an invalid index! index=" + index + " size=" + trackList.size());
        }
        if (index != currentTrackIndex) {

            var change = new Change<>(getTrack(currentTrackIndex), getTrack(index));
            currentTrackIndex = index;
            currentTrackChangeEvent.invoke(change);
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

    public void addCurrentTrackChangeListener(ChangeListener<Change<Track>> listener) {
        currentTrackChangeEvent.addListener(listener);
    }

    public void removeCurrentTrackChangeListener(ChangeListener<Change<Track>> listener) {
        currentTrackChangeEvent.removeListener(listener);
    }

    public void addTrackListChangeListener(ChangeListener<Change<Track>> listener) {
        trackListChangeEvent.addListener(listener);
    }

    public void removeTrackListChangeListener(ChangeListener<Change<Track>> listener) {
        trackListChangeEvent.addListener(listener);
    }


}
