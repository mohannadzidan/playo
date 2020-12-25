package playo;

import javafx.collections.ObservableMap;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Track {
    public static final Image DEFAULT_MUSIC_ART = new Image(PlayOApp.class.getResource("/icons/default-music-art.png").toString());
    private final URL url;
    private final ObservableMap<String, Object> metadata;

    public Track(@NotNull URL url) {
        this.url = url;
        Media media = new Media(url.toString());
        this.metadata = media.getMetadata();
    }

    public URL getURL() {
        return url;
    }

    public String getTitle() {
        return Objects.requireNonNullElse((String) metadata.get("title"), getFileName());
    }

    public String getArtist() {
        return Objects.requireNonNullElse((String) metadata.get("artist"), "Unknown");
    }

    public String getAlbum() {
        return Objects.requireNonNullElse((String) metadata.get("album"), "-");
    }

    public String getYear() {
        return Objects.requireNonNullElse(metadata.get("year"), "-").toString();
    }

    public Image getMusicArt() {
        return Objects.requireNonNullElse((Image) metadata.get("image"), DEFAULT_MUSIC_ART);
    }

    public String getFileName() {
        try {
            var decoded = java.net.URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.name());
            return decoded.substring(decoded.lastIndexOf("/") + 1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url.toString().substring(url.toString().lastIndexOf("/") + 1);
    }

    public ObservableMap<String, Object> getMetadata() {
        return metadata;
    }

}
