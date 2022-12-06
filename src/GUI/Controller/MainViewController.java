package GUI.Controller;

import BE.Playlist;
import BE.Song;
import GUI.Model.PlaylistModel;
import GUI.Model.SongModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class MainViewController extends BaseController implements Initializable {

    //all of the instance variables. Available everywhere in the class
    @FXML
    private TextField filterSearch;
    @FXML
    private Slider timeSlider;
    @FXML
    private TableView songsTable;
    @FXML
    private TableView playlistTable;
    @FXML
    private Slider volumeSlider;
    @FXML
    private TableColumn songTitleColumn;
    @FXML
    private TableColumn songArtistColumn;
    @FXML
    private TableColumn songCategoryColumn;
    @FXML
    private TableColumn songTimeColumn;
    @FXML
    private Button CloseBtn;
    @FXML
    private Button searchBtn;
    @FXML
    private TableColumn playlistNameColumn;
    @FXML
    private TableColumn playlistSongsAmountColumn;
    @FXML
    private TableColumn playlistTimeColumn;
    @FXML
    private Button playBtn;
    @FXML
    private Button forwardBtn;
    @FXML
    private Label currentSongPlaying;
    @FXML
    private Button backBtn;
    @FXML
    private TableView songsInsidePlaylist;
    @FXML
    private TableColumn titleColumn;


    private SongModel musicModel;
    private PlaylistModel playlistModel;


    private File directory;
    private File[] files;

    boolean isPlaying;
    private MediaPlayer mediaPlayer;
    private Media media;
    private ArrayList<File> songs;
    private List<Song> allSongs;
    private List<String> allSongsFilepaths;
    private int songNumber;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    public void setup() {
        updateSongList();
        updatePlaylist();
        //mediaPlayermetod();
    }

    private void mediaPlayermetod() {
        musicModel = getModel().getSongModel();
        songs = new ArrayList<>();
        allSongs = new ArrayList<>();
        allSongsFilepaths = new ArrayList<>();

        //Song s = (Song) songsTable.getFocusModel().getFocusedItem();
        //String filepath = s.getFilePath();
        //directory = new File(filepath);


        if (musicModel.getSongsList() != null) {
            System.out.println("entered the if");
            for (Song song : musicModel.getSongsList()) {
                System.out.println("entered the for-loop");
                allSongs.add(song);
                directory = new File(song.getFilePath());
                System.out.println("Filepath: " + song.getFilePath());
                files = directory.listFiles();
                if (files != null) {
                    System.out.println("entered the last statement");
                    for (File file : files) {
                        System.out.println("is this printing?");
                        songs.add(file);
                        System.out.println(file.getName() + file + "ingen fejl her");
                    }
                }
            }
        }

        directory = new File("C:\\Users\\aneho\\OneDrive\\Dokumenter\\Music");
        //directory = new File("C:\\Users\\aneho\\OneDrive\\Dokumenter\\Music\\lastSummer.mp3");

        files = directory.listFiles();  //stores files in directory

        if (files != null) {
            for (File file : files) {
                songs.add(file);
                System.out.println(file.getName() + file + "\t\t\t der er fejl her");
            }
        }
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        boolean isPlaying = false;
        //controlling volumenslider
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }
        });

        //Controlling timeslider
        timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            }
        });
        //Display the song on the label
        //currentSongPlaying.setText(songs.get(songNumber).getName());
        placeholders();

    }

    /**
     * sets the lable text when the tableview are empty
     */
    private void placeholders() {
        songsInsidePlaylist.setPlaceholder(new Label("No songs on playlist"));
        songsTable.setPlaceholder(new Label("No songs added yet"));
        playlistTable.setPlaceholder(new Label("No playlist created yet"));
    }

    /**
     * updates the songs in playlist tableview
     */
    private void updateSongsInPlaylist() {
        try {
            Playlist pl = (Playlist) playlistTable.getFocusModel().getFocusedItem();
            playlistModel = getModel().getPlaylistModel();

            titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));

            songsInsidePlaylist.getColumns().addAll();
            songsInsidePlaylist.setItems(playlistModel.getSongsOnPL(pl.getId()));


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Updates the playlist tableview
     */
    private void updatePlaylist() {
        playlistModel = getModel().getPlaylistModel();

        playlistNameColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        playlistSongsAmountColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfSongs"));
        playlistTimeColumn.setCellValueFactory(new PropertyValueFactory<>("timeLength"));


        playlistTable.getColumns().addAll();
        playlistTable.setItems(playlistModel.getObservablePlaylists());
    }

    /**
     * updates the songs tableview
     */
    private void updateSongList() {
        musicModel = getModel().getSongModel();

        songArtistColumn.setCellValueFactory(new PropertyValueFactory<>("Artist"));
        songTitleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        songCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("Category"));
        songTimeColumn.setCellValueFactory(new PropertyValueFactory<>("Length"));

        songsTable.getColumns().addAll();
        songsTable.setItems(musicModel.getObservableSongs());
    }

    public void updatePLList() {
        playlistTable.getColumns().removeAll();

        playlistTable.getColumns().addAll();
        playlistTable.setItems(playlistModel.getObservablePlaylists());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * opens a new window to show a detailed view when adding a new song
     *
     * @param event
     * @throws IOException
     */
    public void handleNewSong(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/SongView.fxml"));
        AnchorPane pane = (AnchorPane) loader.load();

        SongViewController controller = loader.getController();
        controller.setModel(super.getModel());
        controller.setup();

        // Create the dialog stage
        Stage dialogWindow = new Stage();
        dialogWindow.setTitle("New song");
        dialogWindow.initModality(Modality.WINDOW_MODAL);
        dialogWindow.initOwner(((Node) event.getSource()).getScene().getWindow());
        Scene scene = new Scene(pane);
        dialogWindow.setScene(scene);
        dialogWindow.showAndWait();
    }

    /**
     * opens a new window to create a new playList in
     *
     * @param event
     * @throws IOException
     */
    public void handleNewPlaylist(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/PlaylistView.fxml"));
        AnchorPane pane = (AnchorPane) loader.load();
        PlaylistViewController controller = loader.getController();
        controller.setModel(super.getModel());
        controller.setup();

        // Create the dialog stage
        Stage dialogWindow = new Stage();
        dialogWindow.setTitle("New playlist");
        dialogWindow.initModality(Modality.WINDOW_MODAL);
        dialogWindow.initOwner(((Node) event.getSource()).getScene().getWindow());
        Scene scene = new Scene(pane);
        dialogWindow.setScene(scene);
        dialogWindow.showAndWait();


    }

    public void handleMovePlaylistUp(ActionEvent actionEvent) {
    }

    public void handleMovePlaylistDown(ActionEvent actionEvent) {
    }

    /**
     * delets a song from a playlist
     *
     * @param actionEvent
     */
    public void handleDeleteSongOnPlaylist(ActionEvent actionEvent) {
        Playlist pl = (Playlist) playlistTable.getFocusModel().getFocusedItem();
        Song s = (Song) songsInsidePlaylist.getFocusModel().getFocusedItem();
        int sId = s.getId();
        int plId = pl.getId();

        musicModel.removeSongFromPlaylist(sId, plId);
        updateSongsInPlaylist();
    }

    /**
     * opens a window where you can edit the name of the playlist
     *
     * @param actionEvent
     * @throws IOException
     */
    public void handleEditPlaylist(ActionEvent actionEvent) throws IOException {
        Playlist selectedPlaylist = (Playlist) playlistTable.getFocusModel().getFocusedItem();
        if (selectedPlaylist != null) {
            playlistModel.setSelectedPlaylist(selectedPlaylist);
            playlistModel.setShouldEdit(true);
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/PlaylistView.fxml"));
        AnchorPane pane = (AnchorPane) loader.load();

        PlaylistViewController controller = loader.getController();
        controller.setModel(super.getModel());
        controller.setup();

        // Create the dialog stage
        Stage dialogWindow = new Stage();
        dialogWindow.setTitle("New playlist");
        dialogWindow.initModality(Modality.WINDOW_MODAL);
        dialogWindow.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        Scene scene = new Scene(pane);
        dialogWindow.setScene(scene);
        dialogWindow.showAndWait();

        //return selectedPlaylist;
    }

    /**
     * delets the selected playlist
     *
     * @param actionEvent
     * @throws Exception
     */
    public void handleDeletePlaylist(ActionEvent actionEvent) throws Exception {
        Playlist pl = (Playlist) playlistTable.getFocusModel().getFocusedItem();


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + pl.getTitle() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            playlistModel.deletePlaylist(pl);
        }
    }

    /**
     * adds the selected song to the selected playlist
     *
     * @param actionEvent
     */
    public void handleAddSongToPlaylist(ActionEvent actionEvent) {
        Playlist pl = (Playlist) playlistTable.getFocusModel().getFocusedItem();
        Song s = (Song) songsTable.getFocusModel().getFocusedItem();

        int sId = s.getId();
        int plId = pl.getId();

        musicModel.addSongToPlaylist(sId, plId);
        updateSongsInPlaylist();
    }


    /**
     * opens a new window where you can edit the selected song
     *
     * @param actionEvent
     * @throws IOException
     */
    public void handleEditSong(ActionEvent actionEvent) throws IOException {
        Song selectedSong = (Song) songsTable.getSelectionModel().getSelectedItem();
        musicModel.setSelectedSong(selectedSong);
        musicModel.setShouldEdit(true);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/SongView.fxml"));
        AnchorPane pane = (AnchorPane) loader.load();

        SongViewController controller = loader.getController();
        controller.setModel(super.getModel());
        controller.setup();

        // Create the dialog stage
        Stage dialogWindow = new Stage();
        dialogWindow.setTitle("Edit song");
        dialogWindow.initModality(Modality.WINDOW_MODAL);
        dialogWindow.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        Scene scene = new Scene(pane);
        dialogWindow.setScene(scene);
        dialogWindow.showAndWait();
    }

    /**
     * deletes the selected song
     *
     * @param actionEvent
     * @throws Exception
     */
    public void handleDeleteSong(ActionEvent actionEvent) throws Exception {
        Song s = (Song) songsTable.getFocusModel().getFocusedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + s.getArtist() + " - " + s.getTitle() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            musicModel.deleteSong(s);
        }
    }


    /**
     * closes the window when the button is pressed
     *
     * @param actionEvent
     */
    public void handleClose(ActionEvent actionEvent) {
        Stage stage = (Stage) CloseBtn.getScene().getWindow();
        stage.close();
    }


    /**
     * on the first click of the button it searches through the song table to fit the query on the second click of the button it clears the search query
     *
     * @param actionEvent
     */
    public void handleSearch(ActionEvent actionEvent) {
        if (searchBtn.getText().equals("Search")) {
            if (filterSearch.getText() != null) {
                String search = filterSearch.getText().toLowerCase();
                songsTable.setItems(musicModel.filteredSongs(search));
            }
            searchBtn.setText("Clear");
        } else if (searchBtn.getText().equals("Clear")) {
            filterSearch.setText("");
            songsTable.setItems(musicModel.getObservableSongs());
            searchBtn.setText("Search");
        }
    }


    /**
     * on the first click of the button plays selected song on the second click pauses the song
     */


    public void playSong() {
        //begin to track the progress
        beginTimer();
        //play and pause the song
        //if song is playing, then set button to pause
        if (isPlaying) {
            playBtn.setText("Play");
            mediaPlayer.pause();
            isPlaying = false;
        } else {
            playBtn.setText("Pause");
            isPlaying = true;
            mediaPlayer.play();
        }
    }

    /**
     * goes to the next song in either the songs tableview or the next song in the playlist
     */
    public void nextSong() {
        if (songNumber < songs.size() - 1) {
            songNumber++;
            shiftSong();
        } else {
            songNumber = 0;
            shiftSong();
        }
    }

    /**
     * switches the song
     */
    public void shiftSong() {
        mediaPlayer.stop();
        media = new Media(songs.get(songNumber).toURI().toString()); //makes a command, possible for mediaPlayer to read
        mediaPlayer = new MediaPlayer(media);   //sets the song
        currentSongPlaying.setText(songs.get(songNumber).getName());
        isPlaying = false;
        playSong();
    }

    /**
     * starts the current song over if the duration is >= 7 seconds of playing it else it goes back to the previous song on the list
     */
    public void previosOrRestartSong() {
        //if more than 7 seconds has passed, the song is restartet, else it is the previos song
        double current = mediaPlayer.getCurrentTime().toSeconds();
        if (current >= 7.0) {
            mediaPlayer.seek(Duration.seconds(0));
            isPlaying = false;
            playSong();
        } else {
            //look for witch song the previous is
            if (songNumber > 0) {
            } else {
                //look for witch song the previos is
                if (songNumber > 0) {
                    songNumber--;
                    shiftSong();
                } else {
                    songNumber = songs.size() - 1;
                    shiftSong();
                }
            }
        }
    }

    /**
     * begins a timer to track the time a song has been playing
     */
    public void beginTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            //Timertask is the task to be executed.
            @Override
            public void run() {
                double end = media.getDuration().toSeconds();
                double current = mediaPlayer.getCurrentTime().toSeconds();
                int endTot = (int) Math.round(end);
                double howFar = current / end;

                System.out.println(Math.round(howFar * 100 * 100) / 100 + " % \tgennem\t" + currentSongPlaying.getText() + "\t Current time: " + Math.round(current) + "\t Total Duration: " + endTot);
                timeSlider.setValue(Math.round(howFar * 100 * 100) / 100);
                if (howFar == 1) {
                    nextSong();
                    cancelTimer();
                }
            }
        };
        //executes the timertask after 1000 milliSeconds = 1 second
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);

    }

    /**
     * stops and resets the timer
     */
    public void cancelTimer() {
        timer.cancel();
    }
    public void timeChanged(MouseDragEvent mouseDragEvent) {
        double howfarNew = Math.round(timeSlider.getValue()) / 100;
        /**
         (current/end)*100=howfar%
         (100 second far/ 200 second end)=0.5 howFarTo1
         end*(howfar%/100)) = current
         current/howfar% = end
         (howfarnew/100) * end = current
         */
        double end = media.getDuration().toSeconds();
                     double newTimeOnSong = end * howfarNew;
                     System.out.println(newTimeOnSong);
                     mediaPlayer.seek(Duration.seconds(newTimeOnSong));
                 }

    /**
     * nayn cat easter egg press the button and get taken to the standard browser of you computer on the given url.
     * @param event
     * @throws InterruptedException
     */
    public void handleCat(ActionEvent event) throws InterruptedException {
        Desktop desktop = Desktop.getDesktop();
        if (desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(URI.create("https://www.nyan.cat/index.php?cat=original"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * shows the songs in a playlist in the songs in playlist tableview
     * @param mouseEvent
     */
    public void handlePlaylistUpdate(MouseEvent mouseEvent) {
        updateSongsInPlaylist();
    }

    /**
     * Moves the current highlighted song UP in the playlist,
     * so the user is able to sort their playlist according to their wishes.
     *
     * @param actionEvent
     */
    public void handleMoveSongUp(ActionEvent actionEvent) {
        //Get focused song
        Song s = (Song) songsInsidePlaylist.getFocusModel().getFocusedItem();
        int index = songsInsidePlaylist.getSelectionModel().getFocusedIndex();

        //Move focused song up if it is not at the top
        if (index > 0) {
            songsInsidePlaylist.getItems().set(index, songsInsidePlaylist.getItems().get(index - 1));
            songsInsidePlaylist.getItems().set(index - 1, s);

            //Keep the item you moved selected
            songsInsidePlaylist.getSelectionModel().select(index - 1);
        } else {
            //Show error if at top and the user tries to move it up
            Alert alert = new Alert(Alert.AlertType.ERROR, "Song already at top of playlist", ButtonType.CANCEL);
            alert.showAndWait();
        }

    }

    /**
     * Moves the current highlighted song DOWN in the playlist,
     * so the user is able to sort their playlist according to their wishes.
     *
     * @param actionEvent
     */
    public void handleMoveSongDown(ActionEvent actionEvent) {
        //Get focused song
        Song s = (Song) songsInsidePlaylist.getFocusModel().getFocusedItem();
        int index = songsInsidePlaylist.getSelectionModel().getFocusedIndex();

        //Move focused song down if it is not at the bottom
        if (index < songsInsidePlaylist.getItems().size() - 1) {
            songsInsidePlaylist.getItems().set(index, songsInsidePlaylist.getItems().get(index + 1));
            songsInsidePlaylist.getItems().set(index + 1, s);

            //Keep the item you moved selected
            songsInsidePlaylist.getSelectionModel().select(index + 1);
        } else {
            //Show error if at bottom and the user tries to move it down
            Alert alert = new Alert(Alert.AlertType.ERROR, "Song already at bottom of playlist", ButtonType.CANCEL);
            alert.showAndWait();
        }

    }
}