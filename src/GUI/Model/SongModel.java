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

    //Constructor
    public SongModel() throws Exception {

        musicManager = new MusicManager();
        songsToBeViewed = FXCollections.observableArrayList();
        songsToBeViewed.addAll(musicManager.getAllSongs());
        categoriesToBeViewed = FXCollections.observableArrayList();
        categoriesToBeViewed.addAll(musicManager.getAllCategories());

    }

    public ObservableList<Song> getObservableSongs() {
        return songsToBeViewed;
    }

    public ObservableList<Category> getObservableCategories() throws Exception {
        return categoriesToBeViewed;
    }

    public void deleteSong(Song s) throws Exception {
        musicManager.deleteSong(s);
        songsToBeViewed.remove(s);
    }

    public void createSong(String title, String artist, String length, String category, String pathToFile) throws Exception {
        songsToBeViewed.add(musicManager.createSong(title, artist, length, category, pathToFile));
    }

    public void createCategory(String name) throws Exception {
        categoriesToBeViewed.add(musicManager.createCategory(name));
    }

    public ObservableList<Song> filteredSongs(String search) {
        //filter function. Searching in both title, artist and categories
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

    //looks at the song the user has selected.
    public Song getSelectedSong() {
        //TODO needs to be a used method.
        //System.out.println("choosen song: " + selectedSong.getTitle());
        return selectedSong;
    }

    public Category getSelectedCategory(){
        return selectedCategory;
    }

    public void setSelectedSong(Song selectedSong) {
        this.selectedSong = selectedSong;
    }

    public Boolean shouldEditSong() {
        if (shouldEdit == false && selectedSong != null) {
            shouldEdit = true;
            return true;
        } else {
            shouldEdit = false;
            return false;
        }
    }

    public void addSongToPlaylist(int sId, int plId) {
        musicManager.addSongToPlaylist(sId, plId);
    }

    public void removeSongFromPlaylist(int sId, int plId) {
        musicManager.removeSongFromPlaylist(sId, plId);
    }

    public void songUpdate(Song updatedSong) throws Exception {
        musicManager.editSong(updatedSong);
    }
}
