package GUI.Model;

import BE.Song;
import BLL.MusicManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.function.Predicate;

public class SongModel {
    private ObservableList<Song> songsToBeViewed, filteredSongs;

    private MusicManager musicManager;

    private Song selectedSong;

    private boolean shouldEdit = false;

    public SongModel() throws Exception {

        musicManager = new MusicManager();
        songsToBeViewed = FXCollections.observableArrayList();
        songsToBeViewed.addAll(musicManager.getAllSongs());
    }

    public ObservableList<Song> getObservableSongs() {
        return songsToBeViewed;
    }

    public void deleteSong(Song s) throws Exception {
        musicManager.deleteSong(s);
        songsToBeViewed.remove(s);
    }

    public void createSong(String title, String artist, String length, String category, String pathToFile) throws Exception {
        songsToBeViewed.add(musicManager.createSong(title, artist, length, category, pathToFile));
    }

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

    public void songUpdate(Song updatedSong) throws Exception {
        // Call BLL
        // Update song in DB
        musicManager.editSong(updatedSong);

        // update TableView
        songsToBeViewed.clear();
        songsToBeViewed.addAll(musicManager.getAllSongs());
    }
    public Song getSelectedSong() {
        return selectedSong;
    }

    public void setSelectedSong(Song selectedSong) {
        this.selectedSong = selectedSong;
    }

    public Boolean shouldEditSong() {

        if (!shouldEdit && selectedSong != null) {
            shouldEdit = true;
        } else {
            shouldEdit = false;
        }
        return shouldEdit;
    }
}
