package BE;

import java.time.Duration;

public class Song {

    private int id;
    private String title;
    private String artist;
    private String length;
    private String category;
    private String filePath;

    //Constructor for Song
    public Song(int id, String title, String artist, String length, String category, String filePath) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.length = length;
        this.category = category;
        this.filePath = filePath;
    }

    /**
     * get the title of the song
     * @return the title of the song
     */
    public String getTitle() {
        return title;
    }

    /**
     * get the artist of the song
     * @return the artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * get the length of the song
     * @return the length of the song
     */
    public String getLength() {
        return length;
    }

    /**
     * get the category of the song
     * @return the category of the song
     */
    public String getCategory() {
        return category;
    }

    /**
     * get the id of the song
     * @return the id of the song
     */
    public int getId() {
        return id;
    }

    /**
     * set the title of the song
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * set the artist of the song
     * @param artist
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * set the length of the song
     * @param length
     */
    public void setLength(String length) {
        this.length = length;
    }

    /**
     * set the category of the song
     * @param category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * get the filepath to the song
     * @return the filepath to the song
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * set the filepath to the song
     * @param filePath
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
