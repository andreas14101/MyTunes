package BLL;

import BE.Category;
import BE.Playlist;
import BE.Song;
import DAL.*;

import java.util.List;

public class MusicManager {
    private ICRUDSongs songDAO;
    private ICRUDPlaylist playlistDAO;

    public MusicManager() {
        songDAO = new SongDAO();
        playlistDAO = new PlaylistDAO();
    }

    /**
     * Gets all the songs
     * @return
     * @throws Exception
     */
    public List<Song> getAllSongs() throws Exception {
        return songDAO.getAllSongs();
    }

    /**
     * Deletes a song
     * @param s
     * @throws Exception
     */
    public void deleteSong(Song s) throws Exception {
        songDAO.deleteSong(s);
    }

    /**
     * Creates a new song object
     * @param title
     * @param artist
     * @param length
     * @param category
     * @param pathToFile
     * @return
     * @throws Exception
     */
    public Song createSong(String title, String artist, String length, String category, String pathToFile) throws Exception {
        return songDAO.createSong(title, artist, length, category, pathToFile);
    }

    /**
     * Edits a song object's information
     * @param updatedSong
     * @throws Exception
     */
    public void editSong(Song updatedSong) throws Exception {
        songDAO.editUpdateSong(updatedSong);
    }

    /**
     * Gets all playlists
     * @return all playlists
     * @throws Exception
     */
    public List<Playlist> getAllPlaylists() throws Exception {
        return playlistDAO.getAllPlaylists();
    }

    /**
     * Deletes a playlist
     * @param pl
     * @throws Exception
     */
    public void deletePlaylist(Playlist pl) throws Exception {
        playlistDAO.deletePlaylist(pl);
    }

    /**
     * Creates a new playlist
     * @param plname
     * @return
     * @throws Exception
     */
    public Playlist createPlaylist(String plname) throws Exception {
        return playlistDAO.createNewPlaylist(plname);
    }

    /**
     * Edits a playlist object's information
     * @param plname
     * @param playlist
     * @return
     * @throws Exception
     */
    public Playlist editPlaylist(String plname, Playlist playlist) throws Exception {
        return playlistDAO.editUpdatePlaylist(plname, playlist);
    }

    /**
     * Gets all categories
     * @return
     * @throws Exception
     */
    public List<Category> getAllCategories() throws Exception {
        return songDAO.getAllCategories();
    }

    /**
     * Creates a new category
     * @param name
     * @return
     * @throws Exception
     */
    public Category createCategory(String name) throws Exception {
        return songDAO.createCategory(name);
    }

    /**
     * Deletes a category
     * @param category
     * @throws Exception
     */
    public void deleteCategory(Category category) throws Exception {
        songDAO.deleteCategory(category);
    }

    /**
     * Get all songs that are on a playlist
     * @param id
     * @return all songs that are on a playlist
     * @throws Exception
     */
    public List<Song> getSongsFromPlaylist(int id) throws Exception {
        return songDAO.getSongsOnPlaylist(id);
    }

    /**
     * Adds a song to a playlist
     * @param sId
     * @param plId
     */
    public void addSongToPlaylist(int sId, int plId) {
        songDAO.addSongToPlayList(sId, plId);
    }

    /**
     * Removes a song from a playlist
     * @param sId
     * @param plId
     */
    public void removeSongFromPlaylist(int sId, int plId) {
        songDAO.removeSongFromPlayList(sId, plId);
    }
}

