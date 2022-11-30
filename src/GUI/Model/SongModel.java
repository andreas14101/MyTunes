package GUI.Model;

import BE.Song;
import BLL.MusicManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SongModel {
    private ObservableList<Song> songsToBeViewed;
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
        musicManager.createSong(title, artist, length, category, pathToFile);
    }



    public Song getSelectedSong() {return selectedSong;}

    public void setSelectedSong(Song selectedSong) {this.selectedSong = selectedSong;}

    public Boolean shouldEditSong()
    {
        if (shouldEdit == false && selectedSong != null)
        {
            shouldEdit = true;
            return true;
        }
        else {
            shouldEdit = false;
            return false;
        }
    }
}
