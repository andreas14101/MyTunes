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
    public Song createSong(String title, String artist, String length, String category, String pathToFile) throws Exception {
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
            stmt.setInt(4,Integer.parseInt(length));
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
        try(Connection conn = databaseConnector.getConnection()){
            String sql = "UPDATE Songs SET Title=?, Artist=?, Category=?, PathToFile=? WHERE Id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Bind parameters
            stmt.setString(1, song.getTitle());
            stmt.setString(2, song.getArtist());
            stmt.setString(3, song.getCategory());
            stmt.setString(4, song.getFilePath());

            stmt.executeUpdate();
        }
        catch (SQLException ex){
            ex.printStackTrace();
            throw new Exception("could not update song", ex);
        }
    }

    @Override
    public void deleteSong(Song s) throws Exception {
        int id = s.getId();
        String sql = "DELETE FROM Songs WHERE Id = "+ id+";";
        try (Connection conn = databaseConnector.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        }
    }
}
