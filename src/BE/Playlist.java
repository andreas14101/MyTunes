package BE;

public class Playlist {

    private int id;
    private String title;
    private int numberOfSongs;
    private String timeLength;

    public Playlist(int id, String title, String timeLength, int numberOfSongs) {
        this.id = id;
        this.title = title;
        this.numberOfSongs = numberOfSongs;
        this.timeLength = timeLength;
    }

    public String getTitle() {
        return title;
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public String getTimeLength() {
        return timeLength;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNumberOfSongs(int numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }

    public void setTimeLength(String timeLength) {
        this.timeLength = timeLength;
    }
}
