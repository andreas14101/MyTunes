package GUI.Model;

import BE.Category;
import BE.Song;
import BLL.MusicManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class SongModel {
    private ObservableList<Song> songsToBeViewed;
    private ArrayList<Song> songs;
    private ObservableList<Category> categoriesToBeViewed;
    private final MusicManager musicManager;
    private Song selectedSong;
    private boolean shouldEdit = false;

    //Constructor
    public SongModel() throws Exception {
        musicManager = new MusicManager();
        songsToBeViewed = FXCollections.observableArrayList();
        songsToBeViewed.addAll(musicManager.getAllSongs());
        categoriesToBeViewed = FXCollections.observableArrayList();
        categoriesToBeViewed.addAll(musicManager.getAllCategories());
        songs = new ArrayList<>();
        songs.addAll(musicManager.getAllSongs());
        getObservableCategories();

    }

    public ArrayList<Song> getSongsList() {
        return songs;
    }

    /**
     * Get the songs that to be viewed in the tableview
     * @return the songsToBeViewed observableList
     */
    public ObservableList<Song> getObservableSongs() {
        return songsToBeViewed;
    }

    /**
     * Get the categories to be viewed
     * @return the categoriesToBeViewed observableList
     * @throws Exception upwards
     */
    public ObservableList<Category> getObservableCategories() throws Exception {
        categoriesToBeViewed = FXCollections.observableArrayList();
        categoriesToBeViewed.addAll(musicManager.getAllCategories());
        return categoriesToBeViewed;
    }

    /**
     * Sends the selected song to BLL as the start of the delete process and removes the song from the observableList
     * @param s the song the user chose to be deleted
     * @throws Exception upwards
     */
    public void deleteSong(Song s) throws Exception {
        musicManager.deleteSong(s);
        songsToBeViewed.remove(s);
    }

    /**
     * Delete the category chosen
     * @param c the category the user chose to be deleted
     * @throws Exception upwards
     */
    public void deleteCategory(Category c) throws Exception {
        musicManager.deleteCategory(c);
    }


    /**
     * Sends a new song to BLL as the start of the create new song process
     */
    public void createSong(String title, String artist, String length, String category, String pathToFile) throws Exception {
        songsToBeViewed.add(musicManager.createSong(title, artist, length, category, pathToFile));
    }

    /**
     * Creates a new category and adds it to the observableList
     * @param name the new category which the user wants to add
     * @throws Exception upwards
     */
    public void createCategory(String name) throws Exception {
        categoriesToBeViewed.add(musicManager.createCategory(name));
    }

    /**
     * Get the songs which contains the search string
     * @param search the keyword the user has input
     * @return return the songs which title, artists or category contains the search string
     */
    public ObservableList<Song> filteredSongs(String search) {
        //Filter function. Searching in both title, artist and categories
        ObservableList<Song> filteredSongs = FXCollections.observableArrayList();
        for (Song song : songsToBeViewed) {
            if (song.getTitle().toLowerCase().contains(search)) {
                filteredSongs.add(song);
            } else if (song.getArtist().toLowerCase().contains(search)) {
                filteredSongs.add(song);
            } else if (song.getCategory().toLowerCase().contains(search)) {
                filteredSongs.add(song);
            }
        }
        return filteredSongs;
    }

    /**
     * Gets the selected song
     * @return selectedSong object
     */
    public Song getSelectedSong() { return selectedSong; }

    /**
     * Sets the selectedSong object
     * @param selectedSong the selected song the user chose
     */
    public void setSelectedSong(Song selectedSong) {
        this.selectedSong = selectedSong;
    }

    /**
     * Get the shouldEdit boolean
     * @return the shouldEdit boolean
     */
    public Boolean getShouldEdit() {
        return shouldEdit;
    }

    /**
     * Sets the shouldEdit boolean according to the value parameter
     * @param value either true or false
     */
    public void setShouldEdit(boolean value) {
        shouldEdit = value;
    }

    /**
     * Calls BLL, so you can add the selected song to the selected playlist
     * @param sId the id of the song the user chose
     * @param plId the id of the playlist the user chose
     */
    public void addSongToPlaylist(int sId, int plId) {
        musicManager.addSongToPlaylist(sId, plId);
    }

    /**
     * Calls BLL, so you can delete the selected song from the selected playlist
     * @param sId the id of the song the user chose
     * @param plId the id of the playlist the user chose
     */
    public void removeSongFromPlaylist(int sId, int plId) {
        musicManager.removeSongFromPlaylist(sId, plId);
    }

    /**
     * Updates the selected song
     * @param updatedSong the song which the user chose to be updated
     * @throws Exception upwards
     */
    public void songUpdate(Song updatedSong) throws Exception {
        //Update song in DB
        musicManager.editSong(updatedSong);
        //Update TableView
        songsToBeViewed.clear();
        songsToBeViewed.addAll(musicManager.getAllSongs());
    }
}
