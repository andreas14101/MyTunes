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

    public List<Category> getAllCategories() throws Exception;

    public Category createCategory(String name) throws Exception;

    public void deleteCategory(Category category) throws Exception;

    public List<Song> getSongsOnPlaylist(int id) throws Exception;

    void addSongToPlayList(int sId, int plId);

    void removeSongFromPlayList(int sId, int plId);
}
