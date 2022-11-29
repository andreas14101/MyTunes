package DAL;

import BE.Song;

import java.util.List;

public interface ICRUDSongs {
    public List<Song> getAllSongs() throws Exception;

    public Song createSong(String title, String artist, int length, String category);

    public void editUpdateSong(Song song) throws Exception;

    public void deleteSong(Song song) throws Exception;
}
