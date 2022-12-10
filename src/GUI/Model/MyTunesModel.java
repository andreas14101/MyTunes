package GUI.Model;


public class MyTunesModel {

    private SongModel songModel;
    private PlaylistModel playlistModel;

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
