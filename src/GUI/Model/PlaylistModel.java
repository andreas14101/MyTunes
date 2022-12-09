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

    public PlaylistModel() throws Exception {
        musicManager = new MusicManager();
        updatePlaylists();
    }

    private void updatePlaylists() throws Exception {
        playlistsToBeViewed = FXCollections.observableArrayList();
        playlistsToBeViewed.addAll(musicManager.getAllPlaylists());
    }

    /**
     * Gets the playlists that need to be shown in the playlist tableview
     * @return the observableList that contains the playlists that should be viewed
     */
    public ObservableList<Playlist> getObservablePlaylists() throws Exception {
        updatePlaylists();
        return playlistsToBeViewed;
    }

    /**
     * Deletes the selected playlist
     * @param pl
     * @throws Exception
     */
    public void deletePlaylist(Playlist pl) throws Exception {
        playlistsToBeViewed.remove(pl);
        musicManager.deletePlaylist(pl);
    }

    /**
     * Creates a new playlist and moves it down to BLL
     * @param plname
     * @throws Exception
     */
    public void createNewPlaylist(String plname) throws Exception {
        playlistsToBeViewed.add(musicManager.createPlaylist(plname));
    }

    /**
     * @return the selected playlist
     */
    public Playlist getSelectedPlaylist() {
        return selectedPlaylist;
    }

    /**
     * Sets the selected playlist
     * @param selectedPlaylist
     */
    public void setSelectedPlaylist(Playlist selectedPlaylist) {
        this.selectedPlaylist = selectedPlaylist;
    }

    /**
     * @return the shouldEdit boolean
     */
    public Boolean getShouldEditPlaylist() {
        return shouldEdit;
    }

    /**
     * Set the shouldEdit boolean value
     * @param value
     */
    public void setShouldEdit(boolean value) {
        shouldEdit = value;
    }

    /**
     * Sends the edited playlist to BLL and updates the playlist tableview
     * @param plname
     * @param pl
     * @throws Exception
     */
    public void editPlaylist(String plname, Playlist pl) throws Exception {
        // Call BLL
        // Update playlist in DB
        musicManager.editPlaylist(plname, pl);

        // Update tableView
        playlistsToBeViewed.clear();
        playlistsToBeViewed.addAll(musicManager.getAllPlaylists());
    }

    /**
     * Gets the songs on the selected playlist
     * @param id
     * @return the songs on the selected playlist
     * @throws Exception
     */
    public ObservableList<Song> getSongsOnPL(int id) throws Exception {
        ObservableList<Song> songsOnPL = FXCollections.observableArrayList();
        songsOnPL.addAll(musicManager.getSongsFromPlaylist(id));
        return songsOnPL;
    }
}
