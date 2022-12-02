package BLL;

import BE.Category;
import BE.Playlist;
import BE.Song;
import DAL.ICRUDPlaylist;
import DAL.ICRUDSongs;
import DAL.MusicDAO;
import javafx.collections.ObservableList;

import java.util.List;

public class MusicManager {

    private ICRUDSongs songDAO;
    private ICRUDPlaylist playlistDAO;

    private Playlist selectedPlaylist;

    public MusicManager() {
        songDAO = new MusicDAO();
        playlistDAO = new MusicDAO();
    }

    public List<Song> getAllSongs() throws Exception {
        return songDAO.getAllSongs();
    }

    public void deleteSong(Song s) throws Exception {
        songDAO.deleteSong(s);
    }

    public Song createSong(String title, String artist, String length, String category, String pathToFile) throws Exception {
        return songDAO.createSong(title, artist, length, category, pathToFile);
    }

    public List<Playlist> getAllPlaylists() throws Exception {
        return playlistDAO.getAllPlaylists();
    }

    public void deletePlaylist(Playlist pl) throws Exception {
        playlistDAO.deletePlaylist(pl);
    }

    public Playlist createPlaylist(String plname) throws Exception {
        return playlistDAO.createNewPlaylist(plname);
    }

    public Playlist editPlaylist(String plname, Playlist playlist) throws Exception {
        return playlistDAO.editUpdatePlaylist(plname, playlist);
    }

    public ObservableList<Category> getAllCategories() throws Exception {
        return songDAO.getAllCategories();
    }

    public Category createCategory(String name) throws Exception {
        return songDAO.createCategory(name);
    }

    public List<Song> getSongsFromPlaylist(int id) throws Exception {
        return songDAO.getSongsOnPlaylist(id);
    }
}

