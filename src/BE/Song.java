package BE;

public class Song {

    private int id;
    private String title;
    private String artist;
    private int length;
    private String category;
    private String pathToFile;

    public Song(int id, String title, String artist, int length, String category, String pathToFile) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.length = length;
        this.category = category;
        this.pathToFile = pathToFile;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getLength() {
        return length;
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

    public void setLength(int length) {
        this.length = length;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
