package DAL;

import BE.Playlist;

import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class PlaylistDAO implements ICRUDPlaylist{

    private MyDatabaseConnector databaseConnector;

    public PlaylistDAO(){
        databaseConnector = new MyDatabaseConnector();
    }
    @Override
    public List<Playlist> getAllPlaylists() throws Exception {
        //Make a list called allPlaylists
        ArrayList<Playlist> allPlaylists = new ArrayList<>();

        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {
            //SQL String to be fed through to the database
            String sql = "SELECT * FROM Playlists;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //Loop through rows from the database result set
            while (rs.next()) {
                //Map DB row to Song Object
                int id = rs.getInt("Id");
                String title = rs.getString("Title");
                Duration time = Duration.ofSeconds(rs.getInt("Time"));
                String timeOutput = time.toMinutesPart() + ":" + time.toSecondsPart();
                int numSongs = rs.getInt("numSongs");


                Playlist pl = new Playlist(id, title, timeOutput, numSongs);
                allPlaylists.add(pl);
            }
            return allPlaylists;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get playlists from database");
        }
    }

    @Override
    public Playlist createNewPlaylist(String plname) throws Exception {

        String sql = "INSERT INTO Playlists(Title, Time, numSongs) VALUES (?,?,?);";
        String Title = plname;
        int id = 0;
        int numSongs = 0;
        String time = "0";
        try (Connection conn = databaseConnector.getConnection()) {

            //Statement is a precompiled SQL statement
            PreparedStatement ps = conn.prepareStatement(sql, RETURN_GENERATED_KEYS);

            ps.setString(1, Title);
            ps.setString(2, time);
            ps.setInt(3, numSongs);


            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            //Gets the next key on the collum index 1(id for playlists) sets id to the new value
            if (rs.next()) {
                id = rs.getInt(1);

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not create playlist" + ex);

        }
        return new Playlist(id, Title, time, numSongs);
    }

    @Override
    public Playlist editUpdatePlaylist(String plname, Playlist playlist) throws Exception {
        //editing in the playlist
        String Title = plname;
        int id = playlist.getId();
        String sql = "UPDATE Playlists SET Title = (?) WHERE Id =" + id + ";";

        try (Connection conn = databaseConnector.getConnection()) {

            //executing the PrepairedStatement in SQL
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, Title);
            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not update playlist" + ex);

        }
        return new Playlist(id, Title, playlist.getTimeLength(), playlist.getNumberOfSongs());
    }

    @Override
    public void deletePlaylist(Playlist playlist) throws Exception {
        int id = playlist.getId();
        String sql = "DELETE FROM Playlists WHERE Id = " + id + ";";
        try (Connection conn = databaseConnector.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        }
    }
}