package BLL;

import BE.Category;
import BE.Playlist;
import BE.Song;
import DAL.*;

import java.util.List;

public class MusicManager {
    private final ICRUDSongs songDAO;
    private final ICRUDPlaylist playlistDAO;

    public MusicManager() {
        songDAO = new SongDAO();
        playlistDAO = new PlaylistDAO();
    }

    /**
     * Gets all the songs
     * @return all the songs from the DB
     * @throws Exception upwards
     */
    public List<Song> getAllSongs() throws Exception {
        return songDAO.getAllSongs();
    }

    /**
     * Deletes a song
     * @param s the song the user chose
     * @throws Exception upwards
     */
    public void deleteSong(Song s) throws Exception {
        songDAO.deleteSong(s);
    }

    /**
     * Creates a new song object
     * @param title of the song
     * @param artist who made the song
     * @param length of the song
     * @param category of the song
     * @param pathToFile of the song
     * @return the song the user created
     * @throws Exception upwards
     */
    public Song createSong(String title, String artist, String length, String category, String pathToFile) throws Exception {
        return songDAO.createSong(title, artist, length, category, pathToFile);
    }

    /**
     * Edits a song object's information
     * @param updatedSong the user chose
     * @throws Exception upwards
     */
    public void editSong(Song updatedSong) throws Exception {
        songDAO.editUpdateSong(updatedSong);
    }

    /**
     * Gets all playlists
     * @return all playlists
     * @throws Exception upwards
     */
    public List<Playlist> getAllPlaylists() throws Exception {
        return playlistDAO.getAllPlaylists();
    }

    /**
     * Deletes a playlist
     * @param pl the playlist the user chose
     * @throws Exception upwards
     */
    public void deletePlaylist(Playlist pl) throws Exception {
        playlistDAO.deletePlaylist(pl);
    }

    /**
     * Creates a new playlist
     * @param plName the name of the new playlist from the users input
     * @return the new playlist
     * @throws Exception upwards
     */
    public Playlist createPlaylist(String plName) throws Exception {
        return playlistDAO.createNewPlaylist(plName);
    }

    /**
     * Edits a playlist object's information
     * @param plName the new name of the playlist from the users input
     * @param playlist the playlist the user chose
     * @throws Exception upwards
     */
    public void editPlaylist(String plName, Playlist playlist) throws Exception {
        playlistDAO.editUpdatePlaylist(plName, playlist);
    }

    /**
     * Gets all categories
     * @return list of categories
     * @throws Exception upwards
     */
    public List<Category> getAllCategories() throws Exception {
        return songDAO.getAllCategories();
    }

    /**
     * Creates a new category
     * @param name of the new category from the users input
     * @return Category
     * @throws Exception upwards
     */
    public Category createCategory(String name) throws Exception {
        return songDAO.createCategory(name);
    }

    /**
     * Deletes a category
     * @param category which the user chose
     * @throws Exception upwards
     */
    public void deleteCategory(Category category) throws Exception {
        songDAO.deleteCategory(category);
    }

    /**
     * Get all songs that are on a playlist
     * @param id the id of the playlist the user chose
     * @return all songs that are on a playlist
     * @throws Exception upwards
     */
    public List<Song> getSongsFromPlaylist(int id) throws Exception {
        return songDAO.getSongsOnPlaylist(id);
    }

    /**
     * Adds a song to a playlist
     * @param sId the id of the song the user chose
     * @param plId the id of the playlist the user chose
     */
    public void addSongToPlaylist(int sId, int plId) {
        songDAO.addSongToPlayList(sId, plId);
    }

    /**
     * Removes a song from a playlist
     * @param sId the id of the song the user chose
     * @param plId the id of the playlist the user chose
     */
    public void removeSongFromPlaylist(int sId, int plId) {
        songDAO.removeSongFromPlayList(sId, plId);
    }
}

