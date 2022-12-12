package GUI.Model;

import BE.Playlist;
import BE.Song;
import BLL.MusicManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PlaylistModel {
    private Playlist selectedPlaylist;
    private boolean shouldEdit = false;
    private final MusicManager musicManager;
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
     * @param pl the playlist the user chose
     * @throws Exception upwards
     */
    public void deletePlaylist(Playlist pl) throws Exception {
        playlistsToBeViewed.remove(pl);
        musicManager.deletePlaylist(pl);
    }

    /**
     * Creates a new playlist and moves it down to BLL
     * @param plName the name the user chose for the playlist
     * @throws Exception upwards
     */
    public void createNewPlaylist(String plName) throws Exception {
        playlistsToBeViewed.add(musicManager.createPlaylist(plName));
    }

    /**
     * @return the selected playlist
     */
    public Playlist getSelectedPlaylist() {
        return selectedPlaylist;
    }

    /**
     * Sets the selected playlist
     * @param selectedPlaylist the playlist the user chose
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
     * @param value either true or false
     */
    public void setShouldEdit(boolean value) {
        shouldEdit = value;
    }

    /**
     * Sends the edited playlist to BLL and updates the playlist tableview
     * @param plName the new name of the playlist the user chose to edit
     * @param pl the playlist the user choose
     * @throws Exception upwards
     */
    public void editPlaylist(String plName, Playlist pl) throws Exception {
        // Call BLL
        // Update playlist in DB
        musicManager.editPlaylist(plName, pl);

        // Update tableView
        playlistsToBeViewed.clear();
        playlistsToBeViewed.addAll(musicManager.getAllPlaylists());
    }

    /**
     * Gets the songs on the selected playlist
     * @param id the id of the playlist the user chose
     * @return the songs on the selected playlist
     * @throws Exception upwards
     */
    public ObservableList<Song> getSongsOnPL(int id) throws Exception {
        ObservableList<Song> songsOnPL = FXCollections.observableArrayList();
        songsOnPL.addAll(musicManager.getSongsFromPlaylist(id));
        return songsOnPL;
    }
}
