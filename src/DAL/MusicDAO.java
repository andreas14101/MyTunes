package DAL;

import BE.Playlist;
import BE.Song;

import java.util.List;

public class MusicDAO implements ICRUDPlaylist, ICRUDSongs{

    private MyDatabaseConnector databaseConnector;
    public MusicDAO() {
        databaseConnector = new MyDatabaseConnector();
    }


    @Override
    public List<Playlist> getAllPlaylists() throws Exception {
        return null;
    }

    @Override
    public Playlist createNewPlaylist(Playlist playlist) throws Exception {
        return null;
    }

    @Override
    public void editUpdatePlaylist(Playlist playlist) throws Exception {

    }

    @Override
    public void deletePlaylist(Playlist playlist) throws Exception {

    }

    @Override
    public List<Song> getAllSongs() throws Exception {
        return null;
    }

    @Override
    public Song createSong(String title, String artist, int length, String category) {
        return null;
    }

    @Override
    public void editUpdateSong(Song song) throws Exception {

    }

    @Override
    public void deleteSong(Song song) throws Exception {

    }
}
