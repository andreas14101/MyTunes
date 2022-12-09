package BE;

import java.time.Duration;

public class Song {
    private int id;
    private String title;
    private String artist;
    private String length;

    private String category;
    private String filePath;

    public Song(int id, String title, String artist, String length, String category, String filePath) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.length = length;
        this.category = category;
        this.filePath = filePath;
    }
    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getCategory() {
        return category;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getLength() {
        return length;
    }
}
