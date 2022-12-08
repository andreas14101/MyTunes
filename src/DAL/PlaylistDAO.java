package DAL;

import BE.Playlist;
import BE.Song;

import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class PlaylistDAO implements ICRUDPlaylist {

    private MyDatabaseConnector databaseConnector;

    public PlaylistDAO() {
        databaseConnector = new MyDatabaseConnector();
    }

    /**
     * Gets all playlists from the database with respective number of songs & length of playlist.
     *
     * @return ArrayList<Playlists> allplaylists
     * @throws Exception
     */
    public List<Playlist> getAllPlaylists() throws Exception {
        //Make a list called allPlaylists
        ArrayList<Playlist> allPlaylists = new ArrayList<>();

        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {
            //SQL String which gets all playlists and their respective songs, along with length of the songs.
            String sql =
                    "SELECT playlistID, Songs.Time, Playlists.Title\n" +
                            "FROM SongPlaylistLink \n" +
                            "JOIN Songs ON SongPlaylistLink.songID = Songs.Id \n" +
                            "JOIN Playlists ON SongPlaylistLink.playlistID = Playlists.Id\n" +
                            "ORDER BY playlistID;";

            //Execute the SQL statement
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //Local variables which help us loop through the playlists
            int lastID = 0;
            int numSongsOnPL = 0;
            int timeOnPL = 0;
            Playlist pl1 = new Playlist(0, "", "", 0);
            boolean firstSong = true;

            //Loop through rows from the database result set
            while (rs.next()) {

                //Map DB row to Playlist Object
                int id = rs.getInt("playlistID");
                String title = rs.getString("Title");
                int time = rs.getInt("Time");

                //If it is the first song of a playlist set lastID Local variable to be equal to id and set firstSong to false
                if (firstSong) {
                    lastID = id;
                    firstSong = false;
                }
                //If the lastID equals the new current id, increment numSongsOnPL and timeOnPL and map the results to pl1
                if (lastID == id) {
                    numSongsOnPL++;
                    timeOnPL = time + timeOnPL;
                    Duration actTimeOnPL = Duration.ofSeconds(timeOnPL);
                    String actTimeOnPL2 = actTimeOnPL.toMinutesPart() + ":" + actTimeOnPL.toSecondsPart();
                    pl1 = new Playlist(id, title, actTimeOnPL2, numSongsOnPL);
                } else {
                    //If the lastID is not equal to id, then add the previous playlist to allPlaylists and reset local variables.
                    allPlaylists.add(pl1);
                    lastID = id;
                    numSongsOnPL = 1;
                    timeOnPL = time;
                    Duration actTimeOnPL = Duration.ofSeconds(timeOnPL);
                    String actTimeOnPL2 = actTimeOnPL.toMinutesPart() + ":" + actTimeOnPL.toSecondsPart();
                    pl1 = new Playlist(id, title, actTimeOnPL2, numSongsOnPL);
                }

            }
            //Add the final playlist instantiated before the while loop to the allPlaylist ArrayList
            allPlaylists.add(pl1);

            //Here we ping the database once more for all the playlists which didn't have any songs attached to them

            //SQL String which gets all Playlists where Id is not equal to the Id's in allPlaylists.
            String sql2 = "SELECT * FROM Playlists WHERE Id != 0";
            String sql3 = "AND Id != ";
            String sql4 = ";";
            //For loop which constructs the SQL String with all Id's from the allPlaylist ArrayList.
            for (int i = 0; i < allPlaylists.size(); i++) {
                sql2 = sql2 + sql3 + allPlaylists.get(i).getId();
            }
            //Produce the final SQL string.
            sql2 = sql2 + sql4;

            //Execute the SQL String.
            Statement stmt2 = conn.createStatement();
            ResultSet rs2 = stmt2.executeQuery(sql2);

            //Map all results from the DB to playlists and add them to allPlaylists.
            while (rs2.next()) {
                int id = rs2.getInt("Id");
                String title = rs2.getString("Title");
                Duration time = Duration.ofSeconds(rs2.getInt("Time"));
                String timeOutput = time.toMinutesPart() + ":" + time.toSecondsPart();
                int numSongs = rs2.getInt("numSongs");
                Playlist pl = new Playlist(id, title, timeOutput, numSongs);
                allPlaylists.add(pl);
            }
            //Return the full set of playlists
            return allPlaylists;
        } catch (
                SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get playlists from database");
        }
    }

    public List<Playlist> getAllPlaylists2() throws Exception {
        //Make a list called allPlaylists
        ArrayList<Playlist> allPlaylists = new ArrayList<>();
        ArrayList<Playlist> allPlaylist2 = new ArrayList<>();

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


        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get playlists from database");
        }
        try (Connection conn2 = databaseConnector.getConnection()) {
            for (int i = 0; i < allPlaylists.size(); i++) {

                int plID = allPlaylists.get(i).getId();
                String plTitle = allPlaylists.get(i).getTitle();
                int numSongsOnPL = 0;
                int timeOnPL = 0;
                String sql2 = "SELECT * FROM SongPlaylistLink JOIN Songs on SongPlaylistLink.songID = Songs.Id WHERE playlistID = " + plID + ";";

                Statement stmt2 = conn2.createStatement();
                ResultSet rs2 = stmt2.executeQuery(sql2);

                while (rs2.next()) {

                    timeOnPL = timeOnPL + rs2.getInt("Time");
                    numSongsOnPL++;
                }
                Duration actTimeOnPL = Duration.ofSeconds(timeOnPL);
                String actTimeOnPL2 = actTimeOnPL.toMinutesPart() + ":" + actTimeOnPL.toSecondsPart();
                Playlist pl2 = new Playlist(plID, plTitle, actTimeOnPL2, numSongsOnPL);
                allPlaylist2.add(pl2);

            }
            return allPlaylist2;

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
