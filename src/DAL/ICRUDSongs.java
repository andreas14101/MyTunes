package DAL;

import BE.Category;
import BE.Song;
import javafx.collections.ObservableList;
import java.util.List;

public interface ICRUDSongs {
    public List<Song> getAllSongs() throws Exception;

    public Song createSong(String title, String artist, String length, String category, String pathToFile) throws Exception;

    public void editUpdateSong(Song song) throws Exception;

    public void deleteSong(Song song) throws Exception;

    public ObservableList<Category> getAllCategories() throws Exception;

    public Category createCategory(String name) throws Exception;

    public List<Song> getSongsOnPlaylist(int id) throws Exception;
}
