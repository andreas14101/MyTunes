package DAL;

import BE.Category;
import BE.Playlist;
import BE.Song;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;


public class MusicDAO implements ICRUDPlaylist, ICRUDSongs {

    private MyDatabaseConnector databaseConnector;

    public MusicDAO() {
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

            PreparedStatement ps = conn.prepareStatement(sql, RETURN_GENERATED_KEYS);

            ps.setString(1, Title);
            ps.setString(2, time);
            ps.setInt(3, numSongs);


            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
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


        String Title = plname;
        int id = playlist.getId();
        String sql = "UPDATE Playlists SET Title = (?) WHERE Id =" + id + ";";

        try (Connection conn = databaseConnector.getConnection()) {

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

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get songs from database");
        }
    }

    @Override
    public Song createSong(String title, String artist, String length, String category, String pathToFile) throws Exception {
        //SQL Statement and initializing id variable.
        String sql = "INSERT INTO Songs (Artist, Title, Category, Time, pathToFile) VALUES (?,?,?,?,?)";
        int id = 0;

        //Establish connection with a try with resources, and creating prepared statement.
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, RETURN_GENERATED_KEYS)) {

            //Bind parameters to the SQL statement.
            stmt.setString(1, artist);
            stmt.setString(2, title);
            stmt.setString(3, category);
            stmt.setInt(4, Integer.parseInt(length));
            stmt.setString(5, pathToFile);

            //Run statement on DB.
            stmt.executeUpdate();

            //Get the new ID from DB.
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not create movie" + ex);
        }

        //Changes the given seconds and changes it into duration and then to a readable String, in the format we want.
        int intDuration = Integer.parseInt(length);
        Duration duration = Duration.ofSeconds(intDuration);
        String outDuration = duration.toMinutesPart() + ":" + duration.toSecondsPart();

        //Generating and returning the new song.
        return new Song(id, title, artist, outDuration, category, pathToFile);
    }

    @Override
    public void editUpdateSong(Song song) throws Exception {
        try (Connection conn = databaseConnector.getConnection()) {
            String sql = "UPDATE Songs SET Title=?, Artist=?, Category=?, PathToFile=? WHERE Id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Bind parameters
            stmt.setString(1, song.getTitle());
            stmt.setString(2, song.getArtist());
            stmt.setString(3, song.getCategory());
            stmt.setString(4, song.getFilePath());
            stmt.setInt(5, song.getId());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("could not update song", ex);
        }
    }


    @Override
    public void deleteSong(Song s) throws Exception {
        int id = s.getId();
        String sql = "DELETE FROM Songs WHERE Id = " + id + ";";
        try (Connection conn = databaseConnector.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Category> getAllCategories() throws Exception {
        //Make a list to return
        ArrayList<Category> allCategories = new ArrayList<>();

        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {
            //SQL String to be fed through to the database
            String sql = "SELECT * FROM Category;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //Loop through rows from the database result set
            while (rs.next()) {
                //Map DB row to Song Object
                int id = rs.getInt("ID");
                String name = rs.getString("Category");

                Category category = new Category(id, name);
                allCategories.add(category);
            }
            return allCategories;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get categories from database");
        }
    }

    public List<Song> getSongsOnPlaylist(int id) throws Exception {
        //Make a list called allSongs
        ArrayList<Song> allSongs = new ArrayList<>();

        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {
            //SQL String to be fed through to the database
            String sql = "SELECT * FROM SongPlaylistLink JOIN Songs on SongPlaylistLink.songID = Songs.Id WHERE playlistID = "+ id +";";

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
                allSongs.add(song);
            }
            return allSongs;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get songs from database");
        }
    }

    @Override
    public Category createCategory(String name) throws Exception {
        //SQL Statement and initializing id variable.
        String sql = "INSERT INTO Category (Category) VALUES (?)";
        int id = 0;

        //Establish connection with a try with resources, and creating prepared statement.
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, RETURN_GENERATED_KEYS)) {

            //Bind parameters to the SQL statement.
            stmt.setString(1, name);

            //Run statement on DB.
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

        //Generating and returning the new song.
        return new Category(id, name);
    }

    @Override
    public void deleteCategory(Category category) throws Exception {
        int id = category.getId();
        String sql = "DELETE FROM Category WHERE Id = " + id + ";";
        try (Connection conn = databaseConnector.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        }
    }

    public void addSongToPlayList(int sId, int plId){
        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {
            //SQL String to be fed through to the database
            String sql = "INSERT INTO SongPlaylistLink(playlistID,songID) VALUES("+plId+","+sId+");";

            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);



        } catch (SQLException ex) {
            ex.printStackTrace();

        }
    }

    public void removeSongFromPlayList(int sId, int plId) {
        //Try with resources on the databaseConnector
        try (Connection conn = databaseConnector.getConnection()) {
            //SQL String to be fed through to the database
            String sql = "DELETE FROM SongPlaylistLink WHERE playlistID = "+plId+" AND songID = "+sId+";";

            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);



        } catch (SQLException ex) {
            ex.printStackTrace();

        }
    }
}
