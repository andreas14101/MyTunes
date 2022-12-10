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


    /**
     * Adds a Playlist to the Database with the parameter name plname
     * @param plname
     * @return Playlist
     * @throws Exception
     */
    @Override
    public Playlist createNewPlaylist(String plname) throws Exception {

        //SQL String which adds the playlist to the DB
        String sql = "INSERT INTO Playlists(Title, Time, numSongs) VALUES (?,?,?);";
        String Title = plname; //Parameter plname is set to be the title
        int id = 0; //Id is autogenerated by DB so it is initially set to 0
        int numSongs = 0; //Number of songs is set to 0, as we create an empty playlist
        String time = "0"; //Length of playlist is 0, as it is empty

        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {

            //Statement is a prepared SQL statement
            PreparedStatement ps = conn.prepareStatement(sql, RETURN_GENERATED_KEYS);

            //Bind parameters to the SQL statement
            ps.setString(1, Title);
            ps.setString(2, time);
            ps.setInt(3, numSongs);

            //Execute Update
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            //Gets the next key on the column index 1(id for playlists) sets id to the new value
            if (rs.next()) {
                id = rs.getInt(1);

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not create playlist" + ex);

        }
        //Return the playlist so it can be fed into the observable list
        return new Playlist(id, Title, time, numSongs);
    }

    /**
     * Edit the name of an existing playlist in the DB to the new parameter plname
     * @param plname
     * @param playlist
     * @return Playlist
     * @throws Exception
     */
    @Override
    public Playlist editUpdatePlaylist(String plname, Playlist playlist) throws Exception {


        String Title = plname; //Set the new title to plname
        int id = playlist.getId(); //Get the Id of the playlist we have chosen to edit

        //SQL String where we update the title of the playlist with the id we got from earlier
        String sql = "UPDATE Playlists SET Title = (?) WHERE Id =" + id + ";";

        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {

            //executing the prepared SQL statement
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, Title);
            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not update playlist" + ex);

        }
        //Return the updated playlist to be fed into the observable list
        return new Playlist(id, Title, playlist.getTimeLength(), playlist.getNumberOfSongs());
    }

    /**
     * Deletes the selected playlist in the DB
     * @param playlist
     * @throws Exception
     */
    @Override
    public void deletePlaylist(Playlist playlist) throws Exception {
        //Get id of the selected playlist
        int id = playlist.getId();

        //SQL String which deletes the playlist with the specific id from the DB
        String sql = "DELETE FROM Playlists WHERE Id = " + id + ";";

        //SQL String which deletes the link between playlists & songs from the DB
        String sql2 = "DELETE FROM SongPlaylistLink WHERE playlistID =" + id + ";";
        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            //Execute the update
            ps2.executeUpdate();
            ps.executeUpdate();
        }
    }
}