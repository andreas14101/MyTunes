package GUI.Model;

import BE.Playlist;
import BE.Song;
import BLL.MusicManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PlaylistModel {


    private Playlist selectedPlaylist;
    private boolean shouldEdit = false;

    private MusicManager musicManager;
    private ObservableList<Playlist> playlistsToBeViewed;
    private ObservableList<String> songsOnList;

    private ObservableList<Song> songsOnPL;

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
        playlistsToBeViewed.add(musicManager.createPlaylist(plname));
    }

    public Playlist getSelectedPlaylist() {
        return selectedPlaylist;
    }

    public void setSelectedPlaylist(Playlist selectedPlaylist) {
        this.selectedPlaylist = selectedPlaylist;
    }

    public Boolean shouldEditPlaylist() {
        if (shouldEdit == false && selectedPlaylist != null) {
            shouldEdit = true;
        } else {
            shouldEdit = false;
        }
        return shouldEdit;
    }

    public void editPlaylist(String plname, Playlist pl) throws Exception {
        playlistsToBeViewed.add(musicManager.editPlaylist(plname, pl));
        playlistsToBeViewed.remove(selectedPlaylist);


    }

    public ObservableList<String> getSongsOnPL(int id) throws Exception {
        songsOnPL = FXCollections.observableArrayList();
        songsOnPL.addAll(musicManager.getSongsFromPlaylist(id));
        songsOnList = FXCollections.observableArrayList();
        for (int i = 0; i < songsOnPL.size(); i++) {
            String SoL = i+1 + ": " + songsOnPL.get(i).getTitle();

            songsOnList.add(SoL);
        }
        return songsOnList;
    }
}
