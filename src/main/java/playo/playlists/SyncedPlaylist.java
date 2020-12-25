package playo.playlists;

import playo.Track;

/**
 * A playlist that syncs its current track and its track
 * list with another playlist while maintaining its order independent
 * of the other playlist
 */
public class SyncedPlaylist extends Playlist {


    private final Playlist syncingPlaylist;

    public SyncedPlaylist(Playlist sync) {
        super();
        syncingPlaylist = sync;
        sync.addTrackListChangeListener((change) -> {
            if (change.getOldValue() == null && change.getNewValue() != null) {
                // track added
                this.addTrack(change.getNewValue());
            } else if (change.getOldValue() != null && change.getNewValue() == null) {
                //track removed
                this.removeTrack(change.getOldValue());
            }
        });

        sync.addCurrentTrackChangeListener((change) -> {
            int index = this.getTrackIndex(change.getNewValue());
            super.moveTo(index);
        });
    }

    @Override
    public void moveTo(int index) {
        syncingPlaylist.moveTo(this.getTrack(index));
    }

    @Override
    public void addTrack(Track track) {
        syncingPlaylist.addTrack(track);
    }

    @Override
    public void removeTrack(int index) {
        syncingPlaylist.removeTrack(this.trackList.get(index));
    }

    public Playlist getSyncingPlaylist() {
        return syncingPlaylist;
    }
}
