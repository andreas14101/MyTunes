package GUI.Model;

import BE.Song;
import BLL.MusicManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.function.Predicate;

public class SongModel {
    private ObservableList<Song> songsToBeViewed, filteredSongs;

    private MusicManager musicManager;

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

    public ObservableList<Song> filteredSongs(String search) {
        filteredSongs = FXCollections.observableArrayList();
        for (int i = 0; i < songsToBeViewed.size(); i++){
            if(songsToBeViewed.get(i).getTitle().toLowerCase().contains(search)){
                filteredSongs.add(songsToBeViewed.get(i));
            } else if (songsToBeViewed.get(i).getArtist().toLowerCase().contains(search)) {
                filteredSongs.add(songsToBeViewed.get(i));
            } else if (songsToBeViewed.get(i).getCategory().toLowerCase().contains(search)) {
                filteredSongs.add(songsToBeViewed.get(i));
            }

        }
        return filteredSongs;
    }
}
