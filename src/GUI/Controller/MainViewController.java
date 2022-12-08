package GUI.Controller;

import BE.ExceptionHandler;
import BE.Playlist;
import BE.Song;
import GUI.Model.PlaylistModel;
import GUI.Model.SongModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    @FXML
    private TextField filterSearch;
    @FXML
    private Slider timeSlider, volumeSlider;
    @FXML
    private TableView songsTable, playlistTable, songsInsidePlaylist;
    @FXML
    private TableColumn songTitleColumn, songArtistColumn, songCategoryColumn, songTimeColumn;
    @FXML
    private TableColumn playlistNameColumn, playlistSongsAmountColumn, playlistTimeColumn;
    @FXML
    private Button CloseBtn, searchBtn, playBtn, forwardBtn, backBtn, editPlaylistBtn, deleteSongFromPlaylistBtn;
    @FXML
    private Button deletePlaylistBtn, deleteSongBtn, EditSongBtn, upArrowBtn, downArrowBtn, leftArrowBtn;
    @FXML
    private Label currentSongPlaying;
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
    private double end;
    private double current;
    private boolean isSomethingChoosen;
    private ExceptionHandler exceptionHandler;

    @Override
    public void setup() {
        updateSongList();
        placeholders();
        //mediaPlayermetod();
        currentSongPlaying.setText("(none) is currently playing");
        exceptionHandler = new ExceptionHandler();
        try {
            updatePlaylist();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        normalSelect();
        disableButtons();
        addListenerBtnPlaylists();
        addListenerBtnSongs();
        addListenerBtnSongsInPlaylist();
        addListenerBtnAddSongsToPl();
    }

    /**
     * Disable buttons that should be usable before a selection in a table have been made
     */
    private void disableButtons(){
        deletePlaylistBtn.setDisable(true);
        deleteSongBtn.setDisable(true);
        EditSongBtn.setDisable(true);
        editPlaylistBtn.setDisable(true);
        deleteSongFromPlaylistBtn.setDisable(true);
        upArrowBtn.setDisable(true);
        downArrowBtn.setDisable(true);
        leftArrowBtn.setDisable(true);
    }

    /**
     * Controls playlist table buttons. Enable or disable edit and delete playlist option,
     * depending on, if a playlist is selected.
     */
    private void addListenerBtnPlaylists(){
        playlistTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Playlist>() {
            public void changed(ObservableValue<? extends Playlist> observable, Playlist oldValue, Playlist newValue) {
                //If something is selected, buttons will be enabled, else they will be disabled
                if (newValue != null) {
                    deletePlaylistBtn.setDisable(false);
                    editPlaylistBtn.setDisable(false);
                } else {
                    deletePlaylistBtn.setDisable(true);
                    editPlaylistBtn.setDisable(true);
                }
            }
        });
    }

    /**
     * Controls Song table buttons. Enable or disable edit and delete song option,
     * depending on, if a song is selected.
     */
    private void addListenerBtnSongs(){
        songsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Song>() {
            public void changed(ObservableValue<? extends Song> observable, Song oldValue, Song newValue) {
                //If something is selected, buttons will be enabled, else they will be disabled
                if (newValue != null) {
                    EditSongBtn.setDisable(false);
                    deleteSongBtn.setDisable(false);
                } else {
                    EditSongBtn.setDisable(true);
                    deleteSongBtn.setDisable(true);
                }
            }
        });
    }

    /**
     * Controls Songs in playlist table buttons. Enable or disable delete and move songs in playlist option,
     * depending on, if a song in a playlist is selected.
     */
    private void addListenerBtnSongsInPlaylist(){
        songsInsidePlaylist.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Song>() {
            public void changed(ObservableValue<? extends Song> observable, Song oldValue, Song newValue) {
                //If something is selected, buttons will be enabled, else they will be disabled
                if (newValue != null) {
                    deleteSongFromPlaylistBtn.setDisable(false);
                    upArrowBtn.setDisable(false);
                    downArrowBtn.setDisable(false);
                } else {
                    deleteSongFromPlaylistBtn.setDisable(true);
                    upArrowBtn.setDisable(true);
                    downArrowBtn.setDisable(true);
                }
            }
        });
    }

    /**
     * Controls button for adding songs to playlists. Enable or disable add song to playlist option,
     * depending on, if a song is selected and a playlist is selected.
     */
    private void addListenerBtnAddSongsToPl(){
        songsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Song>() {
            public void changed(ObservableValue<? extends Song> observable, Song oldValue, Song newValue) {
                //If something is selected, buttons will be enabled, else they will be disabled
                if (newValue != null) {
                    checkIfPlSelected();
                } else {
                    leftArrowBtn.setDisable(true);
                }
            }
        });
    }

    /**
     * Used to check if a playlist is selected. It is called in addListenerBtnAddSongToPl() methode, to check both song
     * and playlist table.
     */
    private void checkIfPlSelected(){
        playlistTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Playlist>() {
            public void changed(ObservableValue<? extends Playlist> observable, Playlist oldValue, Playlist newValue) {
                //If something is selected, buttons will be enabled, else they will be disabled
                if (newValue != null) {
                    leftArrowBtn.setDisable(false);
                } else {
                    leftArrowBtn.setDisable(true);
                }
            }
        });
    }

    private void mediaPlayermetod() {
        isSomethingChoosen = false;

        List<Song> allSongsFromDb = musicModel.getSongsList();
        List<String> filePaths = new ArrayList<>();
        if(allSongsFromDb != null)
        {
            for (Song s: allSongsFromDb)
            {
                filePaths.add(s.getFilePath());
            }
        }

        directory = new File(filePaths.get(songNumber));
        if(directory.exists())
        {
        media = new Media(directory.getAbsoluteFile().toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        }
        //songsInsidePlaylist.getFocusModel().getFocusedItem();


        isSomethingChoosen = true;
        //controlling volumenslider
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }
        });
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
            exceptionHandler.displayError(ex);
        }
    }

    /**
     * Updates the playlist tableview
     */
    private void updatePlaylist() throws Exception {
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

    /**
     * opens a new window to show a detailed view when adding a new song
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleNewSong(ActionEvent event) throws IOException {
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
    @FXML
    private void handleNewPlaylist(ActionEvent event) throws IOException {
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

    /**
     * delets a song from a playlist
     *
     * @param actionEvent
     */
    @FXML
    private void handleDeleteSongOnPlaylist(ActionEvent actionEvent) throws Exception {
        Playlist pl = (Playlist) playlistTable.getFocusModel().getFocusedItem();
        Song s = (Song) songsInsidePlaylist.getFocusModel().getFocusedItem();
        int sId = s.getId();
        int plId = pl.getId();

        musicModel.removeSongFromPlaylist(sId, plId);
        updateSongsInPlaylist();
        updatePlaylist();
    }

    /**
     * opens a window where you can edit the name of the playlist
     *
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    private void handleEditPlaylist(ActionEvent actionEvent) throws IOException {
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
    @FXML
    private void handleDeletePlaylist(ActionEvent actionEvent) throws Exception {
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
    public void handleAddSongToPlaylist(ActionEvent actionEvent) throws Exception {
        //Get chosen playlist & song
        Playlist pl = (Playlist) playlistTable.getSelectionModel().getSelectedItem();
        Song s = (Song) songsTable.getSelectionModel().getSelectedItem();

        //Save the id's of the two into two variables
        int sId = s.getId();
        int plId = pl.getId();

        //Boolean which indicates whether the song is present on the playlist or not
        boolean songPresent = false;

        //For loop which goes through all songs inside the playlist
        for (int i = 0; i < songsInsidePlaylist.getItems().size(); i++) {

            //Get the songs in the playlist and their id's
            Song SiP = (Song) songsInsidePlaylist.getItems().get(i);
            int SiPID = SiP.getId();

            //if the song id and one of the id's from the songs in the playlist match, display a warning.
            if (SiPID == sId) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Song already in Playlist", ButtonType.CANCEL);
                alert.showAndWait();
                //Change boolean which indicates whether the song is present or not to true.
                songPresent = true;
            }

        }
        //If song is not present add it to the playlist.
        if (!songPresent) {
            musicModel.addSongToPlaylist(sId, plId);
            updateSongsInPlaylist();
        }
        updatePlaylist();
    }

    /**
     * opens a new window where you can edit the selected song
     *
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    private void handleEditSong(ActionEvent actionEvent) throws IOException {
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
    @FXML
    private void handleDeleteSong(ActionEvent actionEvent) throws Exception {
        Song s = (Song) songsTable.getFocusModel().getFocusedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + s.getArtist() + " - " + s.getTitle() + "? \nThis will also remove the song in all your playlists.", ButtonType.YES, ButtonType.NO);
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
    @FXML
    private void handleClose(ActionEvent actionEvent) {
        Stage stage = (Stage) CloseBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * on the first click of the button it searches through the song table to fit the query on the second click of the button it clears the search query
     *
     * @param actionEvent
     */
    @FXML
    private void handleSearch(ActionEvent actionEvent) {
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

        //play and pause the song
        //if song is playing, then set button to pause
        if (isPlaying == true && isSomethingChoosen == true) {
            playBtn.setText("Play");
            mediaPlayer.pause();
            isPlaying = false;
        } else if (isPlaying == false && isSomethingChoosen == true) {
            playBtn.setText("Pause");
            isPlaying = true;
            timeMoveAuto();
            timeSkip();
            mediaPlayer.play();
        } else {
            System.out.println("Ingen sange valgt (ง •_•)ง  ( ͡• ͜ʖ ͡• )  o((⊙﹏⊙))o");
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
        currentSongPlaying.setText(allSongs.get(songNumber).getTitle());
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
        } else if (songNumber > 0) {
            songNumber--;
            shiftSong();
        } else {
            songNumber = songs.size() - 1;
            shiftSong();
        }
    }

    /**
     * tracks the time of the song that is currently playing
     */
    public void timeMoveAuto()
    {
      mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
          @Override
          public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
              if(!timeSlider.isValueChanging())
              {
                  double total = mediaPlayer.getTotalDuration().toSeconds();
                  double current = mediaPlayer.getCurrentTime().toSeconds();
                  timeSlider.setMax(total);
                  timeSlider.setValue(current);
              }
          }
      });
    }
    public void timeSkip() {
        timeSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediaPlayer.seek(Duration.seconds(timeSlider.getValue()));
            }
        });
    }

    /**
     * Nayn cat Easter egg press the button and get taken to the standard browser of you computer on the given url.
     *
     * @param event
     * @throws InterruptedException
     */
    @FXML
    private void handleCat(ActionEvent event) throws InterruptedException {
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
     *
     * @param mouseEvent
     */
    @FXML
    private void handlePlaylistUpdate(MouseEvent mouseEvent) {
        updateSongsInPlaylist();
    }

    /**
     * Moves the current highlighted song UP in the playlist,
     * so the user is able to sort their playlist according to their wishes.
     *
     * @param actionEvent
     */
    @FXML
    private void handleMoveSongUp(ActionEvent actionEvent) {
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
    @FXML
    private void handleMoveSongDown(ActionEvent actionEvent) {
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

    public void normalSelect()
    {
        songsTable.setOnMouseClicked(event -> {
            if(event.getClickCount() == 1)
            {
                slectedSong();
            }
        });
        songsInsidePlaylist.setOnMouseClicked(event -> {
            if(event.getClickCount() == 1)
            {
                selectedSongFromPlaylist();
            }
        });
    }


    public void doubleClick(){
        songsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                slectedSong();
            }
        });
        songsInsidePlaylist.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                selectedSongFromPlaylist();
            }
        });
    }

    public void slectedSong() {
        Song s = (Song) songsTable.getFocusModel().getFocusedItem();
        songSelection(s);
    }
    public void selectedSongFromPlaylist() {
        Song s = (Song) songsInsidePlaylist.getFocusModel().getFocusedItem();
        songSelection(s);
    }
    private void songSelection(Song s) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        isSomethingChoosen = true;
        isPlaying = false;
        System.out.println("Choosen song from songtable: " + s.getTitle());
        directory = new File(s.getFilePath());
        media = new Media(directory.getAbsoluteFile().toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        currentSongPlaying.setText(s.getTitle() + "is currently playing");
        playSong();
    }
}