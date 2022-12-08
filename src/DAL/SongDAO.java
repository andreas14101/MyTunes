package DAL;

import BE.Category;
import BE.Song;

import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SongDAO implements ICRUDSongs{

    private MyDatabaseConnector databaseConnector;

    public SongDAO(){
        databaseConnector = new MyDatabaseConnector();
    }

    /**
     * Returns the list of all songs which are in the DB
     * @return List<Song>
     * @throws Exception
     */
    @Override
    public List<Song> getAllSongs() throws Exception {

        //Make a list called allSongs
        ArrayList<Song> allSongs = new ArrayList<>();

        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {

            //SQL String which selects all songs from the DB
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

                //Create and add song to list allSongs
                Song song = new Song(id, title, artist, timeOutput, category, pathToFile);
                allSongs.add(song);
            }
            return allSongs;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get songs from database");
        }
    }

    /**
     * Create a song based on the users input, and store it in the DB
     * @param title
     * @param artist
     * @param length
     * @param category
     * @param pathToFile
     * @return
     * @throws Exception
     */
    @Override
    public Song createSong(String title, String artist, String length, String category, String pathToFile) throws Exception {

        //SQL Statement and initializing id variable.
        String sql = "INSERT INTO Songs (Artist, Title, Category, Time, pathToFile) VALUES (?,?,?,?,?)";
        int id = 0;

        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, RETURN_GENERATED_KEYS)) {

            //Bind parameters to the SQL statement.
            stmt.setString(1, artist);
            stmt.setString(2, title);
            stmt.setString(3, category);
            stmt.setInt(4, Integer.parseInt(length));
            stmt.setString(5, pathToFile);

            //Execute the update into the DB
            stmt.executeUpdate();

            //Get the new ID from DB.
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not create Song" + ex);
        }

        //Changes the given seconds into duration and then to a readable String, in the format we want.
        int intDuration = Integer.parseInt(length);
        Duration duration = Duration.ofSeconds(intDuration);
        String outDuration = duration.toMinutesPart() + ":" + duration.toSecondsPart();

        //Generating and returning the new song to be fed into the observable list
        return new Song(id, title, artist, outDuration, category, pathToFile);
    }

    /**
     * Updates a specified song in the DB with new values placed by the user.
     * @param song
     * @throws Exception
     */
    @Override
    public void editUpdateSong(Song song) throws Exception {

        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {

            //SQL String which updates the chosen song with the parameters the user has changed.
            String sql = "UPDATE Songs SET Title=?, Artist=?, Category=?, PathToFile=? WHERE Id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            //Bind parameters to SQL statement
            stmt.setString(1, song.getTitle());
            stmt.setString(2, song.getArtist());
            stmt.setString(3, song.getCategory());
            stmt.setString(4, song.getFilePath());
            stmt.setInt(5, song.getId());

            //Execute the update in the DB
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not update song", ex);
        }
    }

    /**
     * Delete the chosen song the user has selected, and delete the link to all playlists.
     * @param song
     * @throws Exception
     */
    @Override
    public void deleteSong(Song song) throws Exception {

        //Get the id of the chosen song
        int id = song.getId();

        //SQL String which deletes the song from all songs in the DB
        String sql = "DELETE FROM Songs WHERE Id = " + id + ";";

        //SQL String which deletes the link between the songs and the playlist
        String sql2 = "DELETE FROM SongPlaylistLink WHERE songID = " + id + ";";

        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {

            //Statements are prepared SQL statements
            PreparedStatement ps = conn.prepareStatement(sql);
            PreparedStatement ps2 = conn.prepareStatement(sql2);

            //Execute the update which removes the link between song and playlist first, then remove the song from the DB
            ps2.executeUpdate();
            ps.executeUpdate();
        }
    }

    /**
     * Returns a list of categories from the DB
     * @return
     * @throws Exception
     */
    @Override
    public List<Category> getAllCategories() throws Exception {
        //Make a list to return
        ArrayList<Category> allCategories = new ArrayList<>();

        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {

            //SQL String which gets all categories form the DB
            String sql = "SELECT * FROM Category;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //Loop through rows from the database result set
            while (rs.next()) {
                //Map DB row to Category Object
                int id = rs.getInt("ID");
                String name = rs.getString("Category");

                Category category = new Category(id, name);
                allCategories.add(category);
            }
            return allCategories; //Return categories to be fed to the observable list

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get categories from database");
        }
    }

    /**
     * Create a new category and store it in the DB
     * @param name
     * @return Category
     * @throws Exception
     */
    @Override
    public Category createCategory(String name) throws Exception {
        //SQL Statement and initializing id variable.
        String sql = "INSERT INTO Category (Category) VALUES (?)";
        int id = 0;

        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, RETURN_GENERATED_KEYS)) {

            //Bind parameters to the SQL statement.
            stmt.setString(1, name);

            //Execute the update to the DB
            stmt.executeUpdate();

            //Get the new ID from DB.
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not create category" + ex);
        }
        return new Category(id, name); //Return new Category to be fed to the observable list
    }

    /**
     * Deletes a category from the DB
     * @param category
     * @throws Exception
     */
    @Override
    public void deleteCategory(Category category) throws Exception {
        int id = category.getId(); //Get the id of the category the user has chosen
        //SQL String which deletes the category with the chosen id from the DB
        String sql = "DELETE FROM Category WHERE Id = " + id + ";";
        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {
            //Statement is a prepared SQL statements
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate(); //Execute the update in the DB
        }
    }

    /**
     * Get all songs which are connected to the users chosen playlist
     * @param id
     * @return List<Song>
     * @throws Exception
     */
    @Override
    public List<Song> getSongsOnPlaylist(int id) throws Exception {
        //Make a list called allSongs
        ArrayList<Song> allSongs = new ArrayList<>();

        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {

            String sql = "SELECT * FROM SongPlaylistLink " + //Select all songs which are connected in the SongPlaylistLink
                         "JOIN Songs on SongPlaylistLink.songID = Songs.Id " + //Join the table Songs from the DB to the SongPlaylistLink
                         "WHERE playlistID = "+ id +";"; //But only the songs which are connected to the specific Playlist

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //Loop through rows from the database result set
            while (rs.next()) {
                //Map DB row to Song Object
                int songId = rs.getInt("Id");
                String title = rs.getString("Title");
                Duration time = Duration.ofSeconds(rs.getInt("Time"));
                String timeOutput = time.toMinutesPart() + ":" + time.toSecondsPart();
                String artist = rs.getString("Artist");
                String category = rs.getString("Category");
                String pathToFile = rs.getString("PathToFile");

                Song song = new Song(songId, title, artist, timeOutput, category, pathToFile);
                allSongs.add(song); //Add song to our ArrayList
            }
            return allSongs; //Return allSongs to be fed into the observable list

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get songs from database");
        }
    }

    /**
     * Add the chosen song to the chosen playlist.
     * @param sId
     * @param plId
     */
    @Override
    public void addSongToPlayList(int sId, int plId) {
        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {
            //SQL String which inserts the two id's into SongPlaylistLink
            String sql = "INSERT INTO SongPlaylistLink(playlistID,songID) VALUES("+plId+","+sId+");";

            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql); //Execute insert into DB
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Removes the chosen song from the chosen playlist
     * @param sId
     * @param plId
     */
    @Override
    public void removeSongFromPlayList(int sId, int plId) {
        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {
            //SQL String which deletes the specific row in SongPlaylistLink where the two id's match
            String sql = "DELETE FROM SongPlaylistLink WHERE playlistID = "+plId+" AND songID = "+sId+";";

            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql); //Execute the deletion from the DB
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
