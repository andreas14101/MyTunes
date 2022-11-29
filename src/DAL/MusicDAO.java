package DAL;

import BE.Playlist;
import BE.Song;

import java.sql.*;
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
    public Song createSong(String title, String artist, int length, String category, String pathToFile) throws Exception {

        //SQL Statement and initializing id variable.
        String sql = "INSERT INTO Songs (Artist, Title, Category, Time, pathToFile) VALUES ?,?,?,?,?";
        int id = 0;

        //Establish connection with a try with resources, and creating prepared statement.
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            //Bind parameters to the SQL statement.
            stmt.setString(1,artist);
            stmt.setString(2,title);
            stmt.setString(3,category);
            stmt.setInt(4,length);
            stmt.setString(5,pathToFile);

            //Run statement on DB.
            stmt.executeUpdate();

            //Get the new ID from DB.
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not create movie" + ex);
        }

        //Generating and returning the new song.
        return new Song(id,title,artist,length,category,pathToFile);
    }

    @Override
    public void editUpdateSong(Song song) throws Exception {

    }

    @Override
    public void deleteSong(Song song) throws Exception {

    }
}
