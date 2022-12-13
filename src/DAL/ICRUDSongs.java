package DAL;

import BE.Category;
import BE.Song;
import java.util.List;

public interface ICRUDSongs {
    List<Song> getAllSongs() throws Exception;

    Song createSong(String title, String artist, String length, String category, String pathToFile) throws Exception;

    void editUpdateSong(Song song) throws Exception;

    void deleteSong(Song song) throws Exception;

    List<Category> getAllCategories() throws Exception;

    Category createCategory(String name) throws Exception;

    void deleteCategory(Category category) throws Exception;

    List<Song> getSongsOnPlaylist(int id) throws Exception;

    void addSongToPlayList(int sId, int plId);

    void removeSongFromPlayList(int sId, int plId);
}
