package BLL;

import BE.Song;
import DAL.ICRUDPlaylist;
import DAL.ICRUDSongs;
import DAL.MusicDAO;
import jdk.jfr.Timespan;

import java.time.Duration;
import java.util.List;

public class MusicManager {

    private ICRUDSongs songDAO;
    private ICRUDPlaylist playlistDAO;

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
}

