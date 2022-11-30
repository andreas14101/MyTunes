package GUI.Model;

import BE.Song;
import BLL.MusicManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SongModel {
    private ObservableList<Song> songsToBeViewed;
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
}
