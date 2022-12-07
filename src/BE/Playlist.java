package BE;

public class Playlist {
    private int id;
    private String title;

    private int numberOfSongs;
    private String timeLength;

    //Constructor for a playlist
    public Playlist(int id, String title, String timeLength, int numberOfSongs) {
        this.id = id;
        this.title = title;
        this.numberOfSongs = numberOfSongs;
        this.timeLength = timeLength;
    }

    /**
     * get the title of the playlist
     * @return the title of the playlist
     */
    public String getTitle() {
        return title;
    }

    /**
     * get the number of songs on the playlist
     * @return the number of songs on the playlist
     */
    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    /**
     * get the length of the playlist
     * @return the length of the playlist
     */
    public String getTimeLength() {
        return timeLength;
    }

    /**
     * sets the title of the playlist
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * sets the number of songs on the playlist
     * @param numberOfSongs
     */
    public void setNumberOfSongs(int numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }

    /**
     * sets the length of the playlist
     * @param timeLength
     */
    public void setTimeLength(String timeLength) {
        this.timeLength = timeLength;
    }

    /**
     * gets the id of the playlist
     * @return the id of the playlist
     */
    public int getId() {
        return id;
    }

}