package BLL;

import BE.Song;
import DAL.ICRUDPlaylist;
import DAL.ICRUDSongs;
import DAL.MusicDAO;

public class MusicManager {

    private ICRUDSongs songDAO;
    private ICRUDPlaylist playlistDAO;

    public MusicManager() {
        songDAO = new MusicDAO();
        playlistDAO = new MusicDAO();
    }

    public Song createSong(String title, String artist, int length, String category, String pathToFile) throws Exception {
        return songDAO.createSong(title, artist, length, category, pathToFile);
    }

}
