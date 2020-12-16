package playo;

import java.util.ArrayList;
import java.util.Random;

public class Playlist {
    private final ArrayList<Track> trackList = new ArrayList<>();
    private int currentTrackIndex = 0;
    private static final Random random = new Random();

    public void addTrack(Track track) {
        this.trackList.add(track);
    }

    public void removeTrack(int index) {
        if (index == currentTrackIndex) {
            nextTrack();
        } else if (index < currentTrackIndex) {
            previousTrack();
        }
        trackList.remove(index);
    }

    public void removeTrack(Track track) {
        var index = trackList.indexOf(track);
        if (index >= 0) removeTrack(index);
    }

    public void removeTrack(String fullName) {
        int index = -1;
        for (int i = 0; i < trackList.size(); i++) {
            if (trackList.get(i).getFullName().equals(fullName)) {
                index = i;
                break;
            }
        }
        if (index >= 0) removeTrack(index);
    }

    public Track currentTrack() {
        return trackList.get(currentTrackIndex);
    }

    public Track nextTrack() {
        currentTrackIndex = hasNext() ? ++currentTrackIndex : 0;
        return trackList.get(currentTrackIndex);
    }

    public Track previousTrack() {
        currentTrackIndex = hasPrevious() ? --currentTrackIndex : trackList.size() - 1;
        return trackList.get(currentTrackIndex);
    }

    public Track anyTrack() {
        currentTrackIndex = random.nextInt(trackList.size());
        return trackList.get(currentTrackIndex);
    }

    public boolean hasNext() {
        return currentTrackIndex + 1 < trackList.size();
    }

    public boolean hasPrevious() {
        return currentTrackIndex - 1 >= 0;
    }



}
