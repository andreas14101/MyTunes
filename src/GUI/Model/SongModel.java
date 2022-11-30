package GUI.Model;

import BE.Song;
import BLL.MusicManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SongModel {
    private ObservableList<Song> songsToBeViewed;
    private MusicManager musicManager;

    private Song selectedSong;

    private Boolean shouldEdit = false;

    public SongModel() throws Exception {

        musicManager = new MusicManager();
        songsToBeViewed = FXCollections.observableArrayList();
        songsToBeViewed.addAll(musicManager.getAllSongs());
    }

    public ObservableList<Song> getObservableSongs() {
        return songsToBeViewed;
    }

    public void updateSong(Song updatedSong) throws Exception{
        // Call BLL
        // Update Song in DB
        musicManager.updateSong(updatedSong);

        // Update ObservableList
        songsToBeViewed.clear();
        songsToBeViewed.addAll(musicManager.getAllSongs());
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
