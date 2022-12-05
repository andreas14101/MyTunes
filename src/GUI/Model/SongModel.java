package GUI.Model;

import BE.Category;
import BE.Song;
import BLL.MusicManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SongModel {
    private ObservableList<Song> songsToBeViewed, filteredSongs;
    private ObservableList<Category> categoriesToBeViewed;

    private MusicManager musicManager;

    private Song selectedSong;

    private Category selectedCategory;

    private boolean shouldEdit = false;

    public SongModel() throws Exception {

        musicManager = new MusicManager();
        songsToBeViewed = FXCollections.observableArrayList();
        songsToBeViewed.addAll(musicManager.getAllSongs());
        getObservableCategories();

    }

    /**
     *  get the songs that to be viewed in the tableview
     * @return the songsToBeViewed observableList
     */
    public ObservableList<Song> getObservableSongs() {
        return songsToBeViewed;
    }

    /**
     *  get the categories to be viewed
     * @return the categoriesToBeViewed observableList
     * @throws Exception
     */
    public ObservableList<Category> getObservableCategories() throws Exception {
        categoriesToBeViewed = FXCollections.observableArrayList();
        categoriesToBeViewed.addAll(musicManager.getAllCategories());
        return categoriesToBeViewed;
    }

    /**
     * sends the selected song to BLL as the start of the delete process and removes the song from the observableList
     * @param s
     * @throws Exception
     */
    public void deleteSong(Song s) throws Exception {
        musicManager.deleteSong(s);
        songsToBeViewed.remove(s);
    }


    public void deleteCategory(Category c)throws Exception{
        musicManager.deleteCategory(c);
    }


     /* sends a new song to BLL as the start of the create new song process
     */

    public void createSong(String title, String artist, String length, String category, String pathToFile) throws Exception {
        songsToBeViewed.add(musicManager.createSong(title, artist, length, category, pathToFile));
    }

    /**
     * creates a new category and adds it to the observableList
     * @param name
     * @throws Exception
     */
    public void createCategory(String name) throws Exception {
        categoriesToBeViewed.add(musicManager.createCategory(name));
    }

    /**
     * get the songs that contains the search string
     * @param search
     * @return return the songs who's title contains the search string
     */
    public ObservableList<Song> filteredSongs(String search) {
        filteredSongs = FXCollections.observableArrayList();
        for (int i = 0; i < songsToBeViewed.size(); i++) {
            if (songsToBeViewed.get(i).getTitle().toLowerCase().contains(search)) {
                filteredSongs.add(songsToBeViewed.get(i));
            } else if (songsToBeViewed.get(i).getArtist().toLowerCase().contains(search)) {
                filteredSongs.add(songsToBeViewed.get(i));
            } else if (songsToBeViewed.get(i).getCategory().toLowerCase().contains(search)) {
                filteredSongs.add(songsToBeViewed.get(i));
            }

        }
        return filteredSongs;
    }

    /**
     * gets the selected song
     * @return selectedSong object
     */
    public Song getSelectedSong() {
        return selectedSong;
    }

    /**
     * get the selected category
     * @return selectedCategory object
     */
    public Category getSelectedCategory(){
        return selectedCategory;
    }

    /**
     * sets the selectedSong object
     * @param selectedSong
     */
    public void setSelectedSong(Song selectedSong) {
        this.selectedSong = selectedSong;
    }

    /**
     * get the shouldEdit boolean
     * @return the shouldEdit boolean
     */
    public Boolean getShouldEdit() {
        return shouldEdit;
    }

    /**
     * sets the shouldEdit boolean according to the value parameter
     * @param value
     */
    public void setShouldEdit(boolean value)
    {
        shouldEdit = value;
    }

    public void addSongToPlaylist(int sId, int plId) {
        musicManager.addSongToPlaylist(sId, plId);
    }

    public void removeSongFromPlaylist(int sId, int plId) {
        musicManager.removeSongFromPlaylist(sId, plId);
    }

    public void songUpdate(Song updatedSong) throws Exception {
        // Call BLL
        // Update song in DB
        musicManager.editSong(updatedSong);

        // Update TableView
        songsToBeViewed.clear();
        songsToBeViewed.addAll(musicManager.getAllSongs());
    }
}
