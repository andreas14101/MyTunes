package BE;

public class Playlist {

    private String title;
    private int numberOfSongs;
    private int timeLength;

    public Playlist(String title, int numberOfSongs, int timeLength) {
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

    public int getTimeLength() {
        return timeLength;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNumberOfSongs(int numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }

    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
    }
}
