package BLL;

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
}
