package playo;

public class Track {
    private String name;
    private String artistName;
    private String fullName;
    private String album;
    private int year;

    public Track(String name, String artistName, String fullName, String album, int year) {
        this.name = name;
        this.artistName = artistName;
        this.fullName = fullName;
        this.album = album;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAlbum() {
        return album;
    }

    public long getYear() {
        return year;
    }
}
