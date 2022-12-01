package GUI.Model;

import BE.Playlist;
import BE.Song;
import BLL.MusicManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PlaylistModel {

    private MusicManager musicManager;
    private ObservableList<Playlist> playlistsToBeViewed;
    public PlaylistModel() throws Exception {

        musicManager = new MusicManager();
        playlistsToBeViewed = FXCollections.observableArrayList();
        playlistsToBeViewed.addAll(musicManager.getAllPlaylists());
    }

    public ObservableList<Playlist> getObservablePlaylists() {
        return playlistsToBeViewed;
    }

    public void deletePlaylist(Playlist pl) throws Exception {
        playlistsToBeViewed.remove(pl);
        musicManager.deletePlaylist(pl);
    }

    public void createNewPlaylist(String plname) throws Exception {
        musicManager.createPlaylist(plname);
    }
}
