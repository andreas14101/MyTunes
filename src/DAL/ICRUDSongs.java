package DAL;

import BE.Song;

import java.util.List;

public interface ICRUDSongs {
    public List<Song> getAllSongs() throws Exception;

    public Song createSong(String title, String artist, String length, String category, String pathToFile) throws Exception;

    public void editUpdateSong(Song song) throws Exception;

    public void deleteSong(Song song) throws Exception;

    public List<Song> getSongsOnPlaylist(int id) throws Exception;
}
