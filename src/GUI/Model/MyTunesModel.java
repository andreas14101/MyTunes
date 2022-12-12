package GUI.Model;


public class MyTunesModel {
    private final SongModel songModel;
    private final PlaylistModel playlistModel;

    public MyTunesModel() throws Exception {
        songModel = new SongModel();
        playlistModel = new PlaylistModel();
    }

    public SongModel getSongModel() {
        return songModel;
    }

    public PlaylistModel getPlaylistModel() {
        return playlistModel;
    }
}
