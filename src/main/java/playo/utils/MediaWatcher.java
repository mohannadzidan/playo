package playo.utils;

import javafx.application.Platform;
import playo.Track;
import playo.playlists.Playlist;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

public class MediaWatcher {
    private final Playlist playlist;
    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
    private final ScheduledExecutorService executor;

    public MediaWatcher() throws IOException {
        playlist = new Playlist();
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::processEvents, 0, 2, TimeUnit.SECONDS);
    }

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    /**
     * Register the given directory with the WatchService
     */
    public void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE);
        if (keys.get(key) == null) {
            keys.put(key, dir);
        }
    }

    public void unRegister(Path dir) {
        // TODO fix bug : when unregistering a dir the tracks of this dir must be removed from the playlist
        var keySet = keys.keySet();
        for (Iterator<WatchKey> iterator = keySet.iterator(); iterator.hasNext(); ) {
            WatchKey k = iterator.next();
            var a = keys.get(k);
            if (a.startsWith(dir)) {
                k.cancel();
                iterator.remove();
            }
        }
    }

    private void loadTracks(File f) {
        File[] files;
        if (f.isDirectory() && (files = f.listFiles()) != null) {
            for (File file : files) {
                loadTracks(file);
            }
        } else {
            String path = f.getPath();
            if (path.endsWith(".mp3")) {
                try {
                    playlist.addTrack(new Track(f.toURI().toURL()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    public void registerAll(final Path start) throws IOException {
        loadTracks(new File(start.toUri()));
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Process all events for keys queued to the watcher
     */
    void processEvents() {
        if (keys.isEmpty()) {
            System.out.println("waiting for dirs...");
        }
        // wait for key to be signalled
        WatchKey key;
        try {
            key = watcher.take();
        } catch (InterruptedException x) {
            return;
        }

        Path dir = keys.get(key);
        if (dir == null) {
            System.err.println("WatchKey not recognized!!");
            return;
        }

        for (WatchEvent<?> event : key.pollEvents()) {
            var kind = event.kind();

            // TBD - provide example of how OVERFLOW event is handled
            if (kind == OVERFLOW) {
                continue;
            }
            // Context for directory entry event is the file name of entry
            WatchEvent<Path> ev = cast(event);
            Path name = ev.context();
            Path child = dir.resolve(name);
            try {
                if (kind == ENTRY_CREATE && Files.isDirectory(child, NOFOLLOW_LINKS)) {
                    // if directory is created, and watching recursively, then
                    registerAll(child); // register it and its sub-directories
                } else if (child.getFileName().toString().endsWith(".mp3")) {
                    System.out.format("%s: %s\n", kind.name(), child);
                    if (kind == ENTRY_CREATE) {
                        var track = new Track(child.toUri().toURL());
                        Platform.runLater(() -> playlist.addTrack(track));
                    } else {
                        var filename = child.toUri().toURL().toString();
                        Platform.runLater(() -> playlist.removeTrack(filename));
                    }
                }
            } catch (IOException x) {
                x.printStackTrace();
            }
        }

        // reset key and remove from set if directory no longer accessible
        boolean valid = key.reset();
        if (!valid) {
            keys.remove(key);
            System.out.println("dir removed!");
            // all directories are inaccessible
        }
    }

    public void shutdown() throws IOException {
        executor.shutdown();
        watcher.close();
    }
}
