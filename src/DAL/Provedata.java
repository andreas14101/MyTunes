package DAL;


import BE.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Provedata{
    private final MyDatabaseConnector databaseConnector;
    public List<Song> allSongs;
    public MusicDAO musicDAO;

    public Provedata() {
        musicDAO = new MusicDAO();
        databaseConnector = new MyDatabaseConnector();
    }

    public void insertProvedata() throws Exception {
        allSongs = new ArrayList<>();
        allSongs.add(musicDAO.createSong( "Radioactive", "Ane", "222", "pop", "\"C:\\Users\\aneho\\OneDrive\\Dokumenter\\Music\\radioactive.mp3\""));
        allSongs.add(musicDAO.createSong( "LastSummer", "Ane", "233", "pop", "\"C:\\Users\\aneho\\OneDrive\\Dokumenter\\Music\\lastSummer.mp3\""));
        allSongs.add(musicDAO.createSong( "FinalCoutdown", "Ane", "244", "pop", "\"C:\\Users\\aneho\\OneDrive\\Dokumenter\\Music\\ZfinalCountDown.mp3\""));

    }
    public List<Song> getAllSongs() {
        return allSongs;
    }

    public void deleteEverythingInTable() throws Exception {
        String sql = "TRUNCATE Table Songs;";
        try (Connection conn = databaseConnector.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        }
    }
}