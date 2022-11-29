package DAL;

import BE.Playlist;
import BE.Song;

import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
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
        //Make a list called allSongs
        ArrayList<Song> allSongs = new ArrayList<>();

        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {
            //SQL String to be fed through to the database
            String sql = "SELECT * FROM Songs;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //Loop through rows from the database result set
            while (rs.next()) {
                //Map DB row to Song Object
                int id = rs.getInt("Id");
                String title = rs.getString("Title");
                Duration time = Duration.ofSeconds(rs.getInt("Time"));
                String timeOutput = time.toMinutesPart() + ":" + time.toSecondsPart();
                String artist = rs.getString("Artist");
                String category = rs.getString("Category");
                String pathToFile = rs.getString("PathToFile");

                Song song = new Song(id, title, artist, timeOutput, category, pathToFile);
                allSongs.add(song);
            }
            return allSongs;

        }
        catch (SQLException ex){
        ex.printStackTrace();
        throw new Exception("Could not get songs from database");
        }
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
